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

public class Indoor__homeFragment_user extends Fragment implements EventItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private EventItemAdapter eventItemAdapter;
    private List<EventItem> eventItemsList;

    public Indoor__homeFragment_user() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(EventItem eventItem) {
        // Handle the item click here
        Log.d("Indoor_homeFragment_user", "Item clicked: " + eventItem.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indoor__home_user, container, false);

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
                    if (event != null && "Indoor".equals(event.getInterest())) { // Filter for "Indoor" interest
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
    }
}