package com.sp.silvercloud;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MyEventsFragment extends Fragment implements EventItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private EventItemAdapter adapter;
    private List<EventItem> eventItemList;
    private List<EventItem> filteredEventList;
    private DatabaseReference databaseReference;
    private String userId; // Dynamically set based on the signed-in user
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_events, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if the user is logged in
        if (currentUser != null) {
            userId = currentUser.getUid(); // Get the current user's ID
            Log.d("MyEventsFragment", "User ID: " + userId);
            //Toast.makeText(getContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return rootView; // Return early if the user is not logged in
        }

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventItemList = new ArrayList<>();
        filteredEventList = new ArrayList<>();
        adapter = new EventItemAdapter(getContext(), filteredEventList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize SearchView
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText); // Filter the events as the user types
                return true;
            }
        });

        // Fetch user's registered events
        fetchUserRegisteredEvents();

        return rootView;
    }

    private void fetchUserRegisteredEvents() {
        Log.d("MyEventsFragment", "Fetching registered events for user: " + userId);

        // Fetch the event IDs from the user's events node
        databaseReference.child("users").child(userId).child("events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> registeredEventIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventId = snapshot.getKey(); // Get the eventId
                            if (eventId != null) {
                                registeredEventIds.add(eventId);
                                Log.d("MyEventsFragment", "Event ID fetched: " + eventId);
                            }
                        }

                        Log.d("MyEventsFragment", "Total event IDs fetched: " + registeredEventIds.size());

                        // Fetch event details using eventIds
                        fetchEventDetails(registeredEventIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("MyEventsFragment", "loadPost:onCancelled", error.toException());
                        Toast.makeText(getContext(), "Failed to fetch events: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchEventDetails(List<String> eventIds) {
        eventItemList.clear();  // Clear existing list
        final int totalEvents = eventIds.size(); // Total number of events to fetch
        final int[] eventsFetched = {0}; // Counter for fetched events

        if (totalEvents == 0) {
            // No events found for the user
            Log.d("MyEventsFragment", "No events found for the user.");
            updateRecyclerView();
            return;
        }

        for (String eventId : eventIds) {
            Log.d("MyEventsFragment", "Fetching event details for event ID: " + eventId);

            databaseReference.child("events").child(eventId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                if (eventItem != null) {
                                    eventItemList.add(eventItem);
                                    Log.d("MyEventsFragment", "Event added: " + eventItem.getTitle());
                                }
                            } else {
                                Log.d("MyEventsFragment", "Event not found for event ID: " + eventId);
                            }

                            // Increment the counter
                            eventsFetched[0]++;

                            // Check if all events have been fetched
                            if (eventsFetched[0] == totalEvents) {
                                updateRecyclerView(); // Update RecyclerView only after all events are fetched
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("MyEventsFragment", "loadPost:onCancelled", error.toException());
                            Toast.makeText(getContext(), "Failed to fetch event details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void filterEvents(String query) {
        filteredEventList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredEventList.addAll(eventItemList);  // Show all events if query is empty
        } else {
            // Filter the list based on the query
            for (EventItem eventItem : eventItemList) {
                if (eventItem.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredEventList.add(eventItem);
                }
            }
        }
        adapter.notifyDataSetChanged();  // Notify the adapter to update the list
    }

    private void updateRecyclerView() {
        Log.d("MyEventsFragment", "Updating RecyclerView with " + eventItemList.size() + " events.");

        if (eventItemList.isEmpty()) {
            // Show a message or placeholder if no events are found
            Toast.makeText(getContext(), "No events found", Toast.LENGTH_SHORT).show();
        } else {
            filteredEventList.clear();
            filteredEventList.addAll(eventItemList);  // Show all events
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(EventItem eventItem) {
        // Handle item click event, e.g., navigate to event details
        Log.d("MyEventsFragment", "Item clicked: " + eventItem.getTitle());

        // Create and show EventDetailsFragment
        EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventItem);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, eventDetailsFragment)  // Replace with EventDetailsFragment
                .addToBackStack(null)
                .commit();
    }
}