package com.sp.silvercloud;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.MenuItem;
import android.view.GestureDetector;
import android.view.MotionEvent;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.text.TextUtils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.sp.silvercloud.databinding.ActivityMainOrganiserBinding;




public class HomeFragment extends Fragment implements EventItemAdapter.OnItemClickListener  {
    private ImageButton filterButton;
    private DrawerLayout drawerLayout; // for the navigation drawer
    protected NavigationView navigationView;
    private ActivityMainOrganiserBinding binding;
    private RecyclerView recyclerView;
    private EventItemAdapter adapter;
    private EventItemAdapter eventItemAdapter;
    private List<EventItem> eventItemList;
    private List<EventItem> eventItemsList;
    private List<EventItem> filteredEventList;  // New list to store filtered data
    private List<EventItem> eventList;
    private DatabaseReference databaseReference;
    private String userInterest = "All"; // Default filter
    private GestureDetector gestureDetector;
    private GestureListener gestureListener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);

        if (recyclerView == null) {
            Log.e("HomeFragment", "RecyclerView is NULL! Check fragment_home.xml.");
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        eventItemsList = new ArrayList<>();
        // Initialize eventItemAdapter and list for side nav panel
        eventItemAdapter = new EventItemAdapter(getContext(), eventItemsList, (EventItemAdapter.OnItemClickListener) getActivity());
        recyclerView.setAdapter(eventItemAdapter);

        // Load default events (Outdoor initially)
        loadEvents("Outdoor");





        eventList = new ArrayList<>();
        eventItemList = new ArrayList<>();
        filteredEventList = new ArrayList<>(); // Initialize filtered list

        // For filteredEventList, updates adapter
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

        if (TextUtils.isEmpty(query)) {
            filteredEventList.addAll(eventItemList);
        } else {
            for (EventItem eventItem : eventItemList) {
                // Prevent NullPointerException by checking if the title is null
                if (eventItem.getTitle() != null && eventItem.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredEventList.add(eventItem);
                }
            }
        }

        adapter.notifyDataSetChanged();
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

    public void loadEvents(String interest) {
        databaseReference = FirebaseDatabase.getInstance().getReference("events").child("interest");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventItemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventItem event = snapshot.getValue(EventItem.class);

                    if (event == null) {
                        Log.e("Firebase", "Null event found, skipping...");
                        continue;
                    }

                    String eventInterest = event.getInterest();
                    if (eventInterest == null) {
                        Log.e("Firebase", "Event missing 'interest' field, skipping...");
                        continue;
                    }

                    if (eventInterest.equalsIgnoreCase(interest)) {
                        eventItemsList.add(event);
                    }
                }
                eventItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading data", databaseError.toException());
            }
        });
    }

}

