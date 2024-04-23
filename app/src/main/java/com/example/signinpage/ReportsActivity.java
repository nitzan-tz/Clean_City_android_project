package com.example.signinpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.net.HttpCookie;
import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnViewReport;
    private RecyclerView rvReport;
    private View.OnClickListener onItemClickListener;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private Report report;
    private ArrayList<Report> reports;
    private User currentUser;
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        rvReport = (RecyclerView) findViewById(R.id.rvReport);

        btnViewReport = (Button) findViewById(R.id.btnViewReport);
        btnViewReport.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        database.child("users").child(mAuth.getUid()).child("reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reports = new ArrayList<Report>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    reports.add(userSnapshot.getValue(Report.class));
                }
                ReportAdapter reportAdapter = new ReportAdapter(reports);
                rvReport.setAdapter(reportAdapter);
                reportAdapter.setOnItemClickListener(onItemClickListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();

                Report reportItem = reports.get(position);

                Intent intent = new Intent(getApplicationContext(), ViewReportActivity.class);
                intent.putExtra("report", reportItem);
                startActivity(intent);

            }
        };
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvReport.setLayoutManager(layoutManager);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnViewReport.getId()) {
            Intent i = new Intent(ReportsActivity.this, ViewReportActivity.class);
            startActivity(i);
        }
        RecyclerView.ViewHolder viewHolder= (RecyclerView.ViewHolder) view.getTag();

    }
}