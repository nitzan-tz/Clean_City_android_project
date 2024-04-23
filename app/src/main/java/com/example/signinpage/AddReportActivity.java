package com.example.signinpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spType;
    private EditText etInformation;
    private EditText etOther;
    private ImageView imgPhoto;
    private Button btnAddPhotoFromGallery;
    private Button btnTakePhoto;
    private Button btnAddReport;
    private FirebaseAuth mAuth;
    private User currentUser;
    private DatabaseReference database;
    private String type;
    private static int REQUEST_TAKE_PHOTO=1;
    private static int PICK_IMG_FILE=2;
    private String currentPhotoPath;
    private Bitmap bitmap;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        spType = (Spinner) findViewById(R.id.spType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.TypeOfHazard,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type = "garbage";
        etInformation = (EditText) findViewById(R.id.etInformation);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnAddPhotoFromGallery = (Button) findViewById(R.id.btnAddPhotoFromGallery);
        btnAddPhotoFromGallery.setOnClickListener(this);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(this);
        btnAddReport = (Button) findViewById(R.id.btnAddReport);
        btnAddReport.setOnClickListener(this);

        btnAddReport.setEnabled(false);
        storageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnAddReport.getId()) {

            SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
            Double[] arrReportLocation = (Double[]) getIntent().getSerializableExtra("arrReportLocation");
            Double latitude = arrReportLocation[0];
            Double longitude = arrReportLocation[1];
            Report report= new Report(mAuth.getUid()+System.currentTimeMillis(), type, etInformation.getText().toString(), downloadUri.toString(), latitude, longitude, currentUser.getId(), currentUser.getName(), false, System.currentTimeMillis(), currentUser.getPhone(), currentUser.getAddress());
            database.child("reports").child(report.getId()).setValue(report);
            database.child("users").child(mAuth.getUid()).child("reports").child(report.getId()).setValue(report);
            Intent intent = new Intent(AddReportActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == btnTakePhoto.getId()) {
            dispatchTakePictureIntent();
        }
        else if(view.getId() == btnAddPhotoFromGallery.getId()){
            openImageFile();
        }
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
            uploadFullImage();
        } else if (requestCode == PICK_IMG_FILE && resultCode == RESULT_OK) {
            setPic();
            Uri uri = data.getData();
            uploadFile(uri, ".jpg" );
        }
    }

    private void openImageFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG_FILE);
    }

    private void uploadFile(Uri uri, String suffix)
    {

        StorageReference riversRef = null;
        if (suffix.equals(".jpg"))
            riversRef = storageRef.child("image/"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + suffix);
        UploadTask uploadTask = riversRef.putFile(uri);
        final StorageReference finalRiversRef = riversRef;
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return finalRiversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    Glide.with(AddReportActivity.this).load(downloadUri).into(imgPhoto);
                    btnAddReport.setEnabled(true);


                    //TODO save the file path at the firebase
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imgPhoto.getWidth();
        int targetH = imgPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imgPhoto.setImageBitmap(bitmap);

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.signinpage.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
/*
github check
 */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadFullImage(){
        Uri file = Uri.fromFile(new File(currentPhotoPath));
        final StorageReference riversRef = storageRef.child("image/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    downloadUri = task.getResult();
                    btnAddReport.setEnabled(true);
                } else {

                }
            }
        });
    }
}