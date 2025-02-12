package com.sp.silvercloud;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

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

public class Outdoor_homeFragment_user extends Fragment implements EventItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private EventItemAdapter eventItemAdapter;
    private List<EventItem> eventItemsList;

    public Outdoor_homeFragment_user() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(EventItem eventItem) {
        // Handle the item click here
        Log.d("Outdoor_homeFragment_user", "Item clicked: " + eventItem.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outdoor_home_user, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventItemsList = new ArrayList<>();
        //eventItemAdapter = new EventItemAdapter(eventItemsList);
        eventItemAdapter = new EventItemAdapter(getContext(), eventItemsList, this);
        recyclerView.setAdapter(eventItemAdapter);

        loadEvents();

        return view;
    }

    private void loadEvents() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventItemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventItem event = snapshot.getValue(EventItem.class);
                    if (event != null && "Outdoor".equals(event.getInterest())) { // Filter for "Outdoor" interest
                        eventItemsList.add(event);
                    }
                }
                eventItemAdapter.notifyItemRangeInserted(0, eventItemsList.size()); // Notify only the new range
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    /*private void loadEvents() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventItemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventItem event = snapshot.getValue(EventItem.class);
                    if (event != null && "Outdoor".equals(event.getInterest())) { // Filter for "Outdoor" interest
                        eventItemsList.add(event);
                    }
                }
                eventItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }*/
}