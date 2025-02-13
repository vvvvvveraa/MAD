package com.sp.silvercloud;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private TextView userNameLabel;
    private EditText userName;
    private EditText userInterests;
    private TextView editSaveButton; // Changed from Button to TextView
    private Button logoutButton;
    private String userId;
    private boolean isEditing = false; // Flag to track edit mode

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Get the current user's ID
        } else {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to login or welcome screen
            Intent intent = new Intent(getActivity(), Welcome.class);
            startActivity(intent);
            requireActivity().finish(); // Close the current activity
            return view; // Return early if user is not logged in
        }

        // Initialize UI elements
        userNameLabel = view.findViewById(R.id.userNameText);
        userName = view.findViewById(R.id.nameField);
        userInterests = view.findViewById(R.id.interestsField);
        editSaveButton = view.findViewById(R.id.editSaveButton); // Correctly initialized as TextView
        logoutButton = view.findViewById(R.id.logoutButton);

        // Initially lock the fields
        toggleEditable(false);

        // Set button click listener for edit/save
        editSaveButton.setOnClickListener(v -> toggleEditMode());

        // Set button click listener for logout
        logoutButton.setOnClickListener(v -> logoutUser());

        // Fetch user data from Firebase
        fetchUserData(userId);

        return view;
    }

    private void fetchUserData(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String interest = snapshot.child("interest").getValue(String.class);
                    if (name != null) {
                        userName.setText(name);
                        userNameLabel.setText(name);
                    }
                    if (interest != null) {
                        userInterests.setText(interest);
                    }
                } else {
                    Log.w("Firebase", "User not found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read user data", error.toException());
            }
        });
    }

    private void toggleEditMode() {
        if (!isEditing) {
            // Enable input fields for editing
            toggleEditable(true);
            editSaveButton.setText("Save");
        } else {
            // Disable input fields and save changes
            toggleEditable(false);
            editSaveButton.setText("Edit");

            // Save updated values to Firebase
            String updatedName = userName.getText().toString();
            String updatedInterests = userInterests.getText().toString();

            if (!TextUtils.isEmpty(updatedName)) {
                databaseReference.child("name").setValue(updatedName);
                userNameLabel.setText(updatedName); // Update label
            }
            if (!TextUtils.isEmpty(updatedInterests)) {
                databaseReference.child("interest").setValue(updatedInterests);
            }

            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
        isEditing = !isEditing; // Toggle the flag
    }

    private void toggleEditable(boolean enabled) {
        userName.setEnabled(enabled);
        userInterests.setEnabled(enabled);
    }

    private void logoutUser() {
        // Sign out the user
        auth.signOut();
        Intent intent = new Intent(getActivity(), Welcome.class);
        startActivity(intent);
        requireActivity().finish(); // Close the current activity
    }
}