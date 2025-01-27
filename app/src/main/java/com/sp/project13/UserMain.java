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
        replaceFragment(new Biz_HomeFragment());

        drawerLayout = findViewById(R.id.drawer_layout); // line 29 to 41 lines are for drawer navigation bar to show
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Biz_HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Set up the navigation drawer item selection listener, this will open different fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.outdoor) {
                    selectedFragment = new Outdoor_homeFragment(); // Replace with your actual fragment class
                }
                else if (item.getItemId() == R.id.indoor) {
                    selectedFragment = new Indoor__homeFragment(); // Replace with your actual fragment class
                }
                else{
                        selectedFragment = new Biz_HomeFragment(); // Default fragment
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }

                // Close the drawer after selection
                drawerLayout.closeDrawers();
                return true;
            }
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