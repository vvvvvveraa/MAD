package com.sp.silvercloud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventDetailsFragment extends Fragment {
    private TextView titleTextView, dateTextView, descriptionTextView, interestTextView, statusTextView;
    private ImageView eventImageView;
    private EditText eventCodeInput;
    private ImageButton backButton;
    private Button joinButton, submitButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String userId;
    private EventItem eventItem;  // Store the event details

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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Get the current user's ID
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view; // Return early if user is not logged in
        }

        // Initialize views
        titleTextView = view.findViewById(R.id.titleTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        interestTextView = view.findViewById(R.id.interestTextView);
        statusTextView = view.findViewById(R.id.attendanceStatus);
        eventImageView = view.findViewById(R.id.eventImageView);
        backButton = view.findViewById(R.id.backButton);
        joinButton = view.findViewById(R.id.joinEventBtn);
        submitButton = view.findViewById(R.id.submitAttendanceBtn);
        eventCodeInput = view.findViewById(R.id.eventCodeEditText);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Set up the back button
        backButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // Set up the submit button
        submitButton.setOnClickListener(v -> {
            String enteredCode = eventCodeInput.getText().toString().trim();
            if (!enteredCode.isEmpty()) {
                verifyEventCode(enteredCode);
            } else {
                Toast.makeText(requireContext(), "Please enter an event code", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the join button
        joinButton.setOnClickListener(v -> {
            if (joinButton.isEnabled()) {
                registerForEvent();
            }
        });

        // Retrieve the event details from arguments
        if (getArguments() != null) {
            eventItem = (EventItem) getArguments().getSerializable("event_item");
            if (eventItem != null) {
                displayEventDetails(eventItem);
                checkIfUserJoined(); // Check if the user has already joined the event
                checkAttendanceStatus(); // Check if the user has already marked attendance
            }
        }

        return view;
    }

    private void displayEventDetails(EventItem eventItem) {
        titleTextView.setText(eventItem.getTitle());
        dateTextView.setText(eventItem.getDate());
        descriptionTextView.setText(eventItem.getFullDescription());
        interestTextView.setText(eventItem.getInterest());

        // Load the event image using Glide
        Glide.with(getContext())
                .load(eventItem.getImageUrl())
                .into(eventImageView);
    }

    private void checkIfUserJoined() {
        if (userId == null || eventItem == null) {
            Log.w("EventDetailsFragment", "User ID or EventItem is null");
            return;
        }

        // Check if the user has already joined the event
        databaseReference.child("users").child(userId).child("events").child(eventItem.getEventId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User has already joined the event
                            joinButton.setText("Joined");
                            joinButton.setEnabled(false); // Disable the button
                            Log.d("EventDetailsFragment", "User has already joined the event");
                        } else {
                            // User has not joined the event
                            joinButton.setText("Join Event");
                            joinButton.setEnabled(true); // Enable the button
                            Log.d("EventDetailsFragment", "User has not joined the event");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("EventDetailsFragment", "Check registration cancelled", error.toException());
                    }
                });
    }

    private void registerForEvent() {
        if (userId == null || eventItem == null) {
            Log.w("EventDetailsFragment", "User ID or EventItem is null");
            return;
        }

        Log.d("EventDetailsFragment", "Attempting to register user: " + userId + " for event: " + eventItem.getEventId());

        // Create a reference to the user's events in the database
        DatabaseReference userEventsRef = databaseReference.child("users").child(userId).child("events");

        // Use the eventId as the key and set its value to true
        userEventsRef.child(eventItem.getEventId()).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EventDetailsFragment", "Successfully registered for event: " + eventItem.getEventId());
                        // Update the button text and disable it
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
                        intent.putExtra("event_title", eventItem.getTitle());
                        intent.putExtra("event_description", eventItem.getFullDescription());
                        startActivity(intent);
                    } else {
                        Log.w("EventDetailsFragment", "Registration failed", task.getException());
                        Toast.makeText(getContext(), "Failed to register for the event", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyEventCode(String enteredCode) {
        if (eventItem == null) {
            Log.w("EventDetailsFragment", "EventItem is null");
            return;
        }

        // Check if the entered code matches the event's code
        if (enteredCode.equals(eventItem.getEventCode())) {
            // Mark attendance in the database
            markAttendance();
        } else {
            Toast.makeText(getContext(), "Invalid Event Code", Toast.LENGTH_SHORT).show();
            Log.w("EventDetailsFragment", "Entered Event Code NOT found.");
        }
    }

    private void markAttendance() {
        if (userId == null || eventItem == null) {
            Log.w("EventDetailsFragment", "User ID or EventItem is null");
            return;
        }

        // Create a reference to the user's attendance in the database
        DatabaseReference attendanceRef = databaseReference.child("users").child(userId).child("attendance").child(eventItem.getEventId());

        // Mark attendance as true
        attendanceRef.setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EventDetailsFragment", "Attendance marked for event: " + eventItem.getEventId());
                        statusTextView.setText("Marked");
                        statusTextView.setTextColor(Color.GREEN);

                        // Disable the submit button after marking attendance
                        submitButton.setEnabled(false);
                        eventCodeInput.setEnabled(false); // Disable the input field as well
                    } else {
                        Log.w("EventDetailsFragment", "Failed to mark attendance", task.getException());
                        Toast.makeText(getContext(), "Failed to mark attendance", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkAttendanceStatus() {
        if (userId == null || eventItem == null) {
            Log.w("EventDetailsFragment", "User ID or EventItem is null");
            return;
        }

        // Check if the user has already marked attendance for this event
        databaseReference.child("users").child(userId).child("attendance").child(eventItem.getEventId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User has already marked attendance
                            statusTextView.setText("Marked");
                            statusTextView.setTextColor(Color.GREEN);

                            // Disable the submit button and input field
                            submitButton.setEnabled(false);
                            eventCodeInput.setEnabled(false);
                            Log.d("EventDetailsFragment", "User has already marked attendance");
                        } else {
                            // User has not marked attendance
                            statusTextView.setText("Not Marked");
                            statusTextView.setTextColor(Color.RED);

                            // Enable the submit button and input field
                            submitButton.setEnabled(true);
                            eventCodeInput.setEnabled(true);
                            Log.d("EventDetailsFragment", "User has not marked attendance");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("EventDetailsFragment", "Check attendance cancelled", error.toException());
                    }
                });
    }
}