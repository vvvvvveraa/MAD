package com.sp.silvercloud;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sp.silvercloud.databinding.ActivityMainBinding;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.sp.silvercloud.databinding.ActivityMainOrganiserBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EventItemAdapter.OnItemClickListener {

    ActivityMainBinding binding;

    // Firebase Database reference
    FirebaseDatabase database;
    DatabaseReference myRef;
    DrawerLayout drawerLayout;
    //ActivityMainOrganiserBinding binding;
    private RecyclerView recyclerView;
    private EventItemAdapter eventItemAdapter;
    private List<EventItem> eventItemsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar); // Only call this if you are using a Toolbar

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout_user);
        NavigationView navigationView = findViewById(R.id.nav_view_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

            if (currentFragment instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment) currentFragment;

                if (item.getItemId() == R.id.nav_outdoor) {
                    homeFragment.loadEvents("Outdoor");  // Update RecyclerView for Outdoor
                } else if (item.getItemId() == R.id.nav_indoor) {
                    homeFragment.loadEvents("Indoor");  // Update RecyclerView for Indoor
                }
            } else {
                replaceFragment(new HomeFragment());  // If not in HomeFragment, load it
            }

            drawerLayout.closeDrawers();
            return true;
        });


        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Log.d("Firebase", "User is logged in: " + user.getEmail());
        } else {
            Log.d("Firebase", "No user logged in.");
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        // Example: Writing data to Firebase
        myRef.setValue("Hello, Firebase!");

        // Reading data from Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the snapshot
                String value = dataSnapshot.getValue(String.class);
                Log.d("Firebase", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

        // Replace the fragment with HomeFragment initially
        replaceFragment(new HomeFragment());

        // Bottom Navigation view handling
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.myevents) {
                replaceFragment(new MyEventsFragment());
            } else if (itemId == R.id.link) {
                replaceFragment(new LinkFragment());
            } else if (itemId == R.id.rewards) {
                replaceFragment(new RewardsFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else {
                return false;
            }
            return true;
        });
    }

    // Method to replace fragments in the frame_layout
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(EventItem eventItem) {

    }

}
