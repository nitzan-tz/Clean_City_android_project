package com.example.signinpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportsViewHolder>{
    private ArrayList<Report> reports;
    private View.OnClickListener mOnItemClickListener;

    public ReportAdapter(ArrayList<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_report_item, parent, false);
        return new ReportsViewHolder(reportView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.tvType.setText(report.getType());
        holder.tvInformation.setText(report.getInformation());
        Glide.with(holder.imgOfReport.getContext() /* context */)
                .load(report.getImgOfReport())
                .into(holder.imgOfReport);

    }

    @Override
    public int getItemCount() {
        if(reports!=null){
            return reports.size();
        }
        return 0;
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public class ReportsViewHolder extends RecyclerView.ViewHolder{
        public TextView tvType;
        public TextView tvInformation;
        public ImageView imgOfReport;
        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvInformation = (TextView) itemView.findViewById(R.id.tvInformation);
            imgOfReport = (ImageView) itemView.findViewById(R.id.imgOfReport);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
}
