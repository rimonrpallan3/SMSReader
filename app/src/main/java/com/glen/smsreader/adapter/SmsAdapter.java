package com.glen.smsreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glen.smsreader.R;
import com.glen.smsreader.model.Sms;

import java.util.ArrayList;
import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    List<Sms> smsArrayList = new ArrayList<>();


    public SmsAdapter(List<Sms> smsArrayList) {
        this.smsArrayList = smsArrayList;
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.content_sms,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
        Sms sms = smsArrayList.get(position);
        holder.tvBody.setText(sms.getMsg());
        holder.tvHeading.setText(sms.getAddress()+" : ");
    }

    @Override
    public int getItemCount() {
        return smsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvHeading;
        TextView tvBody;
        LinearLayout llSmsView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llSmsView = itemView.findViewById(R.id.llSmsView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvBody = itemView.findViewById(R.id.tvBody);
        }
    }
}
