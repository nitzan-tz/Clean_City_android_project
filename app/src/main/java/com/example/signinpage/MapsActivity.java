package com.example.signinpage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private GoogleMap mMap;
    private MapView mvMap;
    private TextView tvLong;
    private TextView tvLat;
    private Button btnMoreActions;
    private Button btnReportCurr;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private DatabaseReference database;
    private ArrayList<Report> reports;
    private DataSnapshot snapshot;
    private FusedLocationProviderClient fusedLocationClient;
    private final int PERMISSION_REQUEST_CODE = 1;
    private Bundle savedInstanceState;
    private LatLng marker = null;
    private LatLng reportLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnReportCurr = (Button) findViewById(R.id.btnReportCurr);
        btnReportCurr.setOnClickListener(this);
        btnMoreActions = (Button) findViewById(R.id.btnMoreActions);
        btnMoreActions.setOnClickListener(this);
        registerForContextMenu(btnMoreActions);
        tvLong = findViewById(R.id.tvLong);
        tvLat = findViewById(R.id.tvLat);
        database = FirebaseDatabase.getInstance().getReference();
        // Initialize mapView
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mvMap = findViewById(R.id.mvMap);
        mvMap.onCreate(mapViewBundle);
        mvMap.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        reportLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(reportLocation).title("reportLocation").draggable(true));
                        // Got last known location. In some rare situations this can be null.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(reportLocation));
                        mMap.setMinZoomPreference(16);
                        tvLat.setText("N" + reportLocation.latitude);
                        tvLong.setText("E" + reportLocation.longitude);
                        if (location != null) {
                            // Logic to handle location object
                            Toast.makeText(getApplicationContext(),"location",Toast.LENGTH_LONG ).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "onMapReady", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        database.child("reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reports = new ArrayList<Report>();
                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    reports.add(report);
                    marker = new LatLng(report.getLatitude(), report.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(marker).title(report.getType().toString()).snippet(report.getId()));
                }
                if (marker != null)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mvMap.onSaveInstanceState(mapViewBundle);
    }

    public void onMarkerDragStart(Marker marker){

    }
    public void onMarkerDrag(Marker marker){

    }
    public void onMarkerDragEnd(Marker marker){
        reportLocation = marker.getPosition();
        tvLat.setText("N" + reportLocation.latitude);
        tvLong.setText("E" + reportLocation.longitude);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (!marker.isDraggable()) {
            String s = marker.getTitle() + "\n" + marker.getSnippet();
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            Report reportItem = null;
            for (Report report : reports) {
                if (report.getId().equals(marker.getSnippet()))
                    reportItem = report;
            }
            Intent intent = new Intent(getApplicationContext(), ViewReportActivity.class);
            intent.putExtra("report", reportItem);
            startActivity(intent);
            return true;
        }
        else {
            reportLocation = marker.getPosition();
            tvLat.setText("N" + reportLocation.latitude);
            tvLong.setText("E" + reportLocation.longitude);
            return true;
        }
   }
    @Override
    public void onMapClick(@NonNull LatLng mapLocation) {
        tvLat.setText("N" + mapLocation.latitude);
        tvLong.setText("E" + mapLocation.longitude);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvMap.onStart();
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.activity_map_menu, menu);
        menu.setHeaderTitle("More Actions");
        menu.add(0, v.getId(), 0, "View reports");
        menu.add(0, v.getId(), 0, "Edit profile");
        menu.add(0, v.getId(), 0, "Sign out");
    }



    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle().equals("View reports"))
        {
            Intent i=new Intent(MapsActivity.this, ReportsActivity.class);
            startActivity(i);
        }
        else if (item.getTitle().equals("Edit profile"))
        {
            Intent i=new Intent(MapsActivity.this, ProfileActivity.class);
            startActivity(i);
        }
        else if (item.getTitle().equals("Sign out"))
        {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnMoreActions.getId()) {
            this.openContextMenu(view);
        }
        else if (view.getId() == btnReportCurr.getId()) {
            Double[] arrReportLocation = {reportLocation.latitude,reportLocation.longitude};
            Intent i=new Intent(MapsActivity.this, AddReportActivity.class);
            i.putExtra("arrReportLocation", arrReportLocation);
            startActivity(i);
        }
    }
}
