package com.example.signinpage;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class ViewReportActivity extends AppCompatActivity {

    private TextView tvType;
    private TextView tvInformation;
    private TextView tvStatus;
    private ImageView imgOfReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        tvType= (TextView) findViewById(R.id.tvType);
        tvInformation= (TextView) findViewById(R.id.tvInformation);
        tvStatus= (TextView)  findViewById(R.id.tvStatus);
        imgOfReport= (ImageView) findViewById(R.id.imgOfReport);
        Report report = (Report) getIntent().getSerializableExtra("report");
        tvType.setText(report.getType());
        tvInformation.setText(report.getInformation());
        if(report.isStatus())
            tvStatus.setText("finished");
        tvStatus.setText("in process");
        Glide.with(this)
                .load(report.getImgOfReport())
                .into(imgOfReport);

    }

}