package com.sp.silvercloud;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private EventItemAdapter adapter;
    private List<EventItem> eventItemList;
    private List<EventItem> filteredEventList;
    private DatabaseReference databaseReference;
    private DatabaseError databaseError;
    private String userId = "user1"; // Static userId for simplicity; replace with dynamic userId if needed
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_events, container, false);

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

        // Fetch user's registered event IDs
        fetchRegisteredEventIds();

        return rootView;
    }

    // Fetch registered event IDs for the user
    /*private void fetchRegisteredEventIds() {
        Log.d("MyEventsFragment", "Fetching registered event IDs for user: " + userId);

        databaseReference.child("registrations").child(userId).child("eventCode")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> eventIds = new ArrayList<>();
                        Log.d("MyEventsFragment", "Raw Firebase Data: " + dataSnapshot.getValue());
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventId = snapshot.getValue(String.class);
                            Log.d("MyEventsFragment", "Event ID retrieved: " + eventId);
                            if (eventId != null) {
                                eventIds.add(eventId);
                            }
                        }

                        // Log the fetched eventIds
                        Log.d("MyEventsFragment", "Fetched event IDs: " + eventIds);

                        // Now fetch the event details using eventIds
                        fetchEventDetails(eventIds);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MyEventsFragment", "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }*/
    @Override
    public void onStart() {
        super.onStart();
        fetchUserRegistrations();
    }

    private void fetchUserRegistrations() {
        Log.d("MyEventsFragment", "Fetching registered event IDs for user: " + userId);

        databaseReference.child("registrations").child(userId).child("eventCode")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> registeredEventCodes = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventId = snapshot.getValue(String.class);
                            if (eventId != null) {
                                registeredEventCodes.add(eventId);
                            }
                        }

                        Log.d("MyEventsFragment", "Fetched event IDs: " + registeredEventCodes);

                        // Query events based on registeredEventCodes
                        databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<EventItem> filteredEvents = new ArrayList<>();

                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    EventItem event = eventSnapshot.getValue(EventItem.class);

                                    if (event != null && registeredEventCodes.contains(event.getEventCode())) {
                                        filteredEvents.add(event);
                                    }
                                }

                                // Update the RecyclerView with the filtered list of events
                                eventItemList.clear();
                                eventItemList.addAll(filteredEvents);
                                filteredEventList.clear();
                                filteredEventList.addAll(eventItemList);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("MyEventsFragment", "Failed to read events", error.toException());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("MyEventsFragment", "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }

    private void fetchRegisteredEventIds() {
        Log.d("MyEventsFragment", "Fetching registered event IDs for user: " + userId);

        databaseReference.child("registrations").child(userId).child("eventCode")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> registeredEventCodes = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String eventId = snapshot.getValue(String.class);
                            if (eventId != null) {
                                registeredEventCodes.add(eventId);
                            }
                        }

                        Log.d("MyEventsFragment", "Fetched event IDs: " + registeredEventCodes);

                        // Query events based on registeredEventCodes
                        databaseReference.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<EventItem> filteredEvents = new ArrayList<>();

                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    EventItem event = eventSnapshot.getValue(EventItem.class);

                                    if (event != null && registeredEventCodes.contains(event.getEventCode())) {
                                        filteredEvents.add(event);
                                    }
                                }

                                // Update the RecyclerView with the filtered list of events
                                eventItemList.clear();
                                eventItemList.addAll(filteredEvents);
                                filteredEventList.clear();
                                filteredEventList.addAll(eventItemList);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("MyEventsFragment", "Failed to read events", error.toException());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("MyEventsFragment", "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }

    // Fetch event details for the provided event IDs
    private void fetchEventDetails(List<String> eventIds) {
        eventItemList.clear();  // Clear existing list
        for (String eventId : eventIds) {
            Log.d("MyEventsFragment", "Fetching event details for event ID: " + eventId);

            // Instead of using eventId, we should fetch the event details using eventCode
            // The eventCode is the key under the 'events' node.
            databaseReference.child("events").orderByChild("eventCode").equalTo(eventId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Loop through the event snapshots returned, even if it's just one
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Log.d("MyEventsFragment", "Event found: " + snapshot.getKey());
                                    EventItem eventItem = snapshot.getValue(EventItem.class);
                                    if (eventItem != null) {
                                        eventItemList.add(eventItem);
                                        Log.d("MyEventsFragment", "Event added: " + eventItem.getTitle());
                                    }
                                }
                            } else {
                                Log.d("MyEventsFragment", "Event not found for event ID: " + eventId);
                            }
                            updateRecyclerView();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("MyEventsFragment", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
        }
    }

    // Filter events based on search query
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

    // Update the RecyclerView after fetching the event details
    private void updateRecyclerView() {
        Log.d("MyEventsFragment", "Updating RecyclerView with " + eventItemList.size() + " events.");

        filteredEventList.clear();
        filteredEventList.addAll(eventItemList);  // Show all events initially
        adapter.notifyDataSetChanged();
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
