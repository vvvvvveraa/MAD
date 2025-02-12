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
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailsFragment extends Fragment {
    private TextView titleTextView, dateTextView, descriptionTextView, interestTextView, statusTextView;
    private ImageView eventImageView;
    private EditText eventCodeInput;
    private ImageButton backButton;
    private Button joinButton, submitButton;

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

        // Initialize IDs
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
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = eventCodeInput.getText().toString().trim();

                if (!enteredCode.isEmpty()) {
                    verifyEventCode(enteredCode);
                } else {
                    Toast.makeText(requireContext(), "Please enter an event code", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                String eventCode = titleTextView.getText().toString();
                registerForEvent(eventCode);
            }
        });

        return view;
    }

    private void verifyEventCode(String enteredCode) {
        databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isCodeValid = false;

                Log.d("FirebaseDebug", "Total Events Retrieved: " + snapshot.getChildrenCount());

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    EventItem eventItem = eventSnapshot.getValue(EventItem.class);

                    if (eventItem != null) {
                        String correctCode = eventItem.getEventCode();  // Get eventCode from EventItem

                        Log.d("FirebaseDebug", "Checking Event Code: " + correctCode);

                        if (enteredCode.equals(correctCode)) {
                            isCodeValid = true;
                            break;
                        }
                    } else {
                        Log.e("FirebaseError", "EventItem is null for snapshot: " + eventSnapshot.getKey());
                    }
                }

                if (isCodeValid) {
                    getActivity().runOnUiThread(() -> {
                        statusTextView.setText("Marked");
                        statusTextView.setTextColor(Color.GREEN);
                        Log.d("FirebaseDebug", "Event Code Matched! Status Updated.");
                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Invalid Event Code", Toast.LENGTH_SHORT).show();
                        Log.w("FirebaseDebug", "Entered Event Code NOT found.");
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Database read cancelled: " + error.getMessage());
            }
        });
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
        //databaseReference.child("registrations").child(userId)
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

    private void registerForEvent(String eventCode) {
        // Use ArrayUnion to add the eventCode to the user's registrations array
        Log.d("EventDetailsFragment", "Attempting to register user: " + userId + " for event: " + eventCode);

        databaseReference.child("registrations").child(userId).child("eventCodes")
                .push()  // Add to the eventCode array
                .setValue(eventCode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EventDetailsFragment", "Successfully registered for event: " + eventCode);
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

    // Method to register the user for the event
    /*private void registerForEvent(String eventCode) {
        // Use ArrayUnion to add the eventCode to the user's registrations array
        Log.d("EventDetailsFragment", "Attempting to register user: " + userId + " for event: " + eventCode);

        databaseReference.child("registrations").child(userId).child("eventCode")
                .push()  // Add to the eventCode array
                .setValue(eventCode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EventDetailsFragment", "Successfully registered for event: " + eventCode);
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
    }*/
}
