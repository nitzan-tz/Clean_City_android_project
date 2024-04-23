package com.example.signinpage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminViewReportActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvType;
    private TextView tvInformation;
    private TextView tvStatus;
    private Button btnChangeStatus;
    private ImageView imgOfReport;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private Report report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_report);
        tvType= (TextView) findViewById(R.id.tvType);
        tvStatus= (TextView) findViewById(R.id.tvStatus);
        tvInformation= (TextView) findViewById(R.id.tvInformation);
        btnChangeStatus= (Button) findViewById(R.id.btnChangeStatus);
        btnChangeStatus.setOnClickListener(this);
        imgOfReport= (ImageView) findViewById(R.id.imgOfReport);
        Report report = (Report) getIntent().getSerializableExtra("report");
        tvType.setText(report.getType());
        tvInformation.setText(report.getInformation());
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        if(report.isStatus())
            tvStatus.setText("finished");
        else
            tvStatus.setText("in process");
        Glide.with(this)
                .load(report.getImgOfReport())
                .into(imgOfReport);

    }

    @Override
    public void onClick(View view) {
        report = (Report) getIntent().getSerializableExtra("report");

        if (view.getId() == btnChangeStatus.getId()) {
            if(report.isStatus()){
                report.setStatus(false);
                database.child("reports").child(report.getId()).child("status").setValue(false);
                database.child("users").child(report.getUserId()).child("reports").child(report.getId()).child("status").setValue(false);
                tvStatus.setText("in process");
            }
            else {
                report.setStatus(true);
                database.child("reports").child(report.getId()).child("status").setValue(true);
                database.child("users").child(report.getUserId()).child("reports").child(report.getId()).child("status").setValue(true);
                tvStatus.setText("finished");
            }

        }
    }
}