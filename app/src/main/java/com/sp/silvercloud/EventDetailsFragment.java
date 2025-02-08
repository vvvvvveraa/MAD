package com.sp.silvercloud;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class EventDetailsFragment extends Fragment {

    private TextView titleTextView, dateTextView, descriptionTextView, eventTextView;
    private ImageView eventImageView;  // Add ImageView for the event image
    private ImageButton backButton;    // Add ImageButton for the back button

    // Factory method to create a new instance of this fragment
    public static EventDetailsFragment newInstance(EventItem eventItem) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("event_item", eventItem);  // Pass the EventItem object
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.titleTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        eventTextView = view.findViewById(R.id.eventCodeTextView);
        eventImageView = view.findViewById(R.id.eventImageView);  // Initialize ImageView
        backButton = view.findViewById(R.id.backButton);  // Get reference to the back button

        // Retrieve event details and display them
        if (getArguments() != null) {
            EventItem eventItem = (EventItem) getArguments().getSerializable("event_item");
            if (eventItem != null) {
                displayEventDetails(eventItem);
            }
        }

        // Set up the back button click listener
        backButton.setOnClickListener(v -> {
            // This will pop the current fragment from the back stack and return to the previous fragment
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void displayEventDetails(EventItem eventItem) {
        // Set text data
        titleTextView.setText(eventItem.getTitle());
        dateTextView.setText(eventItem.getDate());
        descriptionTextView.setText(eventItem.getFullDescription());
        eventTextView.setText(eventItem.getEventCode());

        // Load image using Glide
        Glide.with(getContext())  // Provide context
                .load(eventItem.getImageUrl())  // URL or resource ID
                .into(eventImageView);  // Target ImageView
    }
}
