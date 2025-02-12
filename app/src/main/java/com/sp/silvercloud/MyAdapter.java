package com.sp.silvercloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<Activities> list;


    public MyAdapter(Context context, ArrayList<Activities> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Activities activity = list.get(position);
        holder.activityName.setText(activity.getActivityName());
        holder.activityDes.setText(activity.getActivityDes());
        holder.activityImg.setText(activity.getActivityImg());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView activityName, activityDes, activityImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            activityName = itemView.findViewById(R.id.act_name);
            activityDes = itemView.findViewById(R.id.act_des);
            activityImg = itemView.findViewById(R.id.act_img);
        }
    }

}
