package com.sp.silvercloud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailsFragment extends Fragment {

    private TextView titleTextView, dateTextView, descriptionTextView, interestTextView;
    private ImageView eventImageView;
    private ImageButton backButton;
    private Button joinButton;

    private DatabaseReference databaseReference;
    private String userId = "user1"; // Static userId for simplicity; replace with dynamic userId if needed
    private EventItem eventItem;  // Declare an EventItem to store the event details

    public static EventDetailsFragment newInstance(EventItem eventItem) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("event_item", eventItem);  // Pass the EventItem object
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.titleTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        interestTextView = view.findViewById(R.id.interestTextView);
        eventImageView = view.findViewById(R.id.eventImageView);
        backButton = view.findViewById(R.id.backButton);
        joinButton = view.findViewById(R.id.joinEventBtn);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            eventItem = (EventItem) getArguments().getSerializable("event_item");
            if (eventItem != null) {
                displayEventDetails(eventItem);
                checkIfUserJoined(eventItem.getEventCode()); // Check if user is already registered
            }
        }

        // Set up the back button
        backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // Set up the join button
        joinButton.setOnClickListener(v -> {
            if (joinButton.isEnabled()) {
                String eventCode = interestTextView.getText().toString();
                registerForEvent(eventCode);
            }
        });

        return view;
    }

    private void displayEventDetails(EventItem eventItem) {
        titleTextView.setText(eventItem.getTitle());
        dateTextView.setText(eventItem.getDate());
        descriptionTextView.setText(eventItem.getFullDescription());
        interestTextView.setText(eventItem.getInterest());

        Glide.with(getContext())
                .load(eventItem.getImageUrl())
                .into(eventImageView);
    }

    // Check if user has already joined the event
    private void checkIfUserJoined(String eventCode) {
        databaseReference.child("registrations").child(userId).child("eventCode")
                .orderByValue()
                .equalTo(eventCode)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // If the user has already registered for this event, change the button to "Joined" and disable it
                        if (dataSnapshot.exists()) {
                            joinButton.setText("Joined"); // Change button text to "Joined"
                            joinButton.setEnabled(false); // Disable the button so it can't be clicked again
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("EventDetailsFragment", "Check registration cancelled", databaseError.toException());
                    }
                });
    }

    // Method to register the user for the event
    private void registerForEvent(String eventCode) {
        // Use ArrayUnion to add the eventCode to the user's registrations array
        databaseReference.child("registrations").child(userId).child("eventCode")
                .push()  // Add to the eventCode array
                .setValue(eventCode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If registration is successful, update the button text and disable it
                        joinButton.setText("Joined");
                        joinButton.setEnabled(false);

                        // Get the event start and end times from the eventItem
                        long[] eventTimes = eventItem.parseEventTime();
                        Log.d("EventDetailsFragment", "Event Start Time: " + eventTimes[0]);
                        Log.d("EventDetailsFragment", "Event End Time: " + eventTimes[1]);

                        // Navigate to EventSuccess Activity
                        Intent intent = new Intent(getActivity(), EventSuccess.class);
                        // Pass the event data to EventSuccess
                        intent.putExtra("event_title", eventItem.getTitle());
                        intent.putExtra("event_description", eventItem.getFullDescription());
                        intent.putExtra("event_start_time", eventTimes[0]);
                        intent.putExtra("event_end_time", eventTimes[1]);
                        startActivity(intent);

                        Log.d("EventDetailsFragment", "User successfully registered for the event");
                    } else {
                        Log.w("EventDetailsFragment", "Registration failed", task.getException());
                    }
                });
    }
}
