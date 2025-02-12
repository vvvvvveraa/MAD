package com.sp.silvercloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.EventViewHolder> {

    private final Context context;
    private final List<EventItem> eventItemList;
    private final OnItemClickListener listener;

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(EventItem eventItem);
    }

    // Constructor with click listener
    public EventItemAdapter(Context context, List<EventItem> eventItemList, OnItemClickListener listener) {
        this.context = context;
        this.eventItemList = eventItemList;
        this.listener = listener;  // Initialize the listener correctly
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eventitem_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventItem eventItem = eventItemList.get(position);

        holder.titleTextView.setText(eventItem.getTitle());
        holder.dateTextView.setText(eventItem.getDate());
        holder.interestTextView.setText(eventItem.getInterest());
        holder.newDescriptionTextView.setText(eventItem.getNewDescription());

        // Using Glide to load image from URL
        Glide.with(context).load(eventItem.getImageUrl()).into(holder.imageView);

        // Handle click on the card
        holder.itemView.setOnClickListener(v -> listener.onItemClick(eventItem));  // Click listener
    }

    @Override
    public int getItemCount() {
        return eventItemList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, dateTextView, interestTextView, newDescriptionTextView;
        ImageView imageView;

        public EventViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            interestTextView = itemView.findViewById(R.id.interestTextView);
            newDescriptionTextView = itemView.findViewById(R.id.newDescriptionTextView);
            imageView = itemView.findViewById(R.id.eventImageView);
        }
    }
}
