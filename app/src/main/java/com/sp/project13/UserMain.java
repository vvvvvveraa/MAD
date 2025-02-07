package com.sp.project13;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.sp.project13.databinding.ActivityMainBinding;

public class UserMain extends AppCompatActivity {
    DrawerLayout drawerLayout; // for the navigation drawer
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Only call this if you are using a Toolbar

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load the default fragment
        if (savedInstanceState == null) {
            replaceFragment(new Biz_HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Set up the navigation drawer item selection listener
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.outdoor) {
                selectedFragment = new Outdoor_homeFragment();
            } else if (item.getItemId() == R.id.indoor) {
                selectedFragment = new Indoor__homeFragment();
            } else {
                selectedFragment = new Biz_HomeFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            drawerLayout.closeDrawers();
            return true;
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new Biz_HomeFragment());
            } else if (item.getItemId() == R.id.addAct) {
                replaceFragment(new NewActFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new Biz_ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}