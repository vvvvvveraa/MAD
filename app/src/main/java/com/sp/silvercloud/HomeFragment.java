package com.sp.silvercloud;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;

public class HomeFragment extends Fragment implements EventItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private EventItemAdapter adapter;
    private List<EventItem> eventItemList;
    private List<EventItem> filteredEventList;  // New list to store filtered data
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventItemList = new ArrayList<>();
        filteredEventList = new ArrayList<>(); // Initialize filtered list
        adapter = new EventItemAdapter(getContext(), filteredEventList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        // Fetch data from Firebase and update RecyclerView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventItem eventItem = snapshot.getValue(EventItem.class);
                    if (eventItem != null) {
                        eventItemList.add(eventItem);
                    }
                }
                filteredEventList.clear();
                filteredEventList.addAll(eventItemList);  // Initially, show all events
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Set up SearchView listener to filter events based on user input
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // You can implement additional behavior when the search is submitted if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText);
                return true;
            }
        });

        return rootView;
    }

    // Filter the events based on the search query
    private void filterEvents(String query) {
        filteredEventList.clear();
        if (query.isEmpty()) {
            filteredEventList.addAll(eventItemList);  // If query is empty, show all events
        } else {
            for (EventItem eventItem : eventItemList) {
                // Check if the title or fullDescription contains the query (case-insensitive)
                if (eventItem.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        eventItem.getFullDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredEventList.add(eventItem);
                }
            }
        }
        adapter.notifyDataSetChanged();  // Notify the adapter to update the RecyclerView
    }

    @Override
    public void onItemClick(EventItem eventItem) {
        // Check if eventItem is valid
        if (eventItem != null) {
            Log.d("HomeFragment", "Item clicked: " + eventItem.getTitle());

            // Create a new instance of the EventDetailsFragment and pass the EventItem as an argument
            EventDetailsFragment eventDetailsFragment = EventDetailsFragment.newInstance(eventItem);

            // Perform the fragment transaction to navigate to EventDetailsFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, eventDetailsFragment)  // Replace with EventDetailsFragment
                    .addToBackStack(null)  // Optionally add to back stack for navigation history
                    .commit();
        } else {
            Log.w("HomeFragment", "Clicked item is null");
        }
    }
}

