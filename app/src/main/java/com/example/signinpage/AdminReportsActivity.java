package com.example.signinpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignout;
    private RecyclerView rvReport;
    private View.OnClickListener onItemClickListener;
    private DatabaseReference database;
    private Report report;
    private ArrayList<Report> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);
        rvReport = (RecyclerView) findViewById(R.id.rvReport);

        btnSignout = (Button) findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(this);
        database = FirebaseDatabase.getInstance().getReference();

        database.child("reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reports = new ArrayList<Report>();
                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    reports.add(reportSnapshot.getValue(Report.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                Report reportItem = reports.get(position);

                Intent intent = new Intent(getApplicationContext(), AdminViewReportActivity.class);
                intent.putExtra("report", reportItem);
                startActivity(intent);

            }
        };
        rvReport.setLayoutManager(layoutManager);
        ReportAdapter reportAdapter = new ReportAdapter(reports);
        rvReport.setAdapter(reportAdapter);

        reportAdapter.setOnItemClickListener(onItemClickListener);

    }

    /**
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
        if (view.getId() == btnSignout.getId()) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(AdminReportsActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}
