package com.sp.silvercloud;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventInterest.setText(event.getInterest());
        holder.eventNewDescription.setText(event.getNewDescription());

        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(event.getImageUrl())
                    .into(holder.eventImage);
        } else {
            holder.eventImage.setImageResource(R.drawable.sky); // Ensure this resource exists
        }

        // Set an OnClickListener on the image
        holder.eventImage.setOnClickListener(v -> {
            // Create an Intent to start biz_currentAct
            Intent intent = new Intent(holder.itemView.getContext(), biz_currentAct.class);
            // Pass the event data
            intent.putExtra("imageUrl", event.getImageUrl());
            intent.putExtra("title", event.getTitle());
            intent.putExtra("description", event.getNewDescription());
            intent.putExtra("date", event.getDate()); // Assuming you have a date field in Event
            intent.putExtra("activityCode", event.getEventCode()); // Assuming you have an event code field in Event
            intent.putExtra("eventId", event.getEventId()); // Pass the correct eventId from the Event object
            intent.putExtra("interest", event.getInterest()); // Pass the correct eventId from the Event object
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle;
        TextView eventInterest;
        TextView eventNewDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventInterest = itemView.findViewById(R.id.event_interest);
            eventNewDescription = itemView.findViewById(R.id.event_new_description);
        }
    }
}