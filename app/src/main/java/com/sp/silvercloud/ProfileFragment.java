package com.sp.silvercloud;

import android.content.Intent;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.SharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView editSaveButton, userNameLabel;
    private EditText userName;
    private AutoCompleteTextView userInterests;
    private Button logout;
    private boolean isEditing = false;  // Flag to track edit mode
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize IDs
        userNameLabel = view.findViewById(R.id.userNameText);
        userName = view.findViewById(R.id.nameField);
        userInterests = view.findViewById(R.id.interestsField);
        editSaveButton = view.findViewById(R.id.editSaveButton);
        logout = view.findViewById(R.id.logoutButton);

        // Initially lock the fields
        toggleEditable(false);

        // Set button click listener
        editSaveButton.setOnClickListener(v -> toggleEditMode());

        // Get SharedPreferences to retrieve logged-in user ID
        sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId != null) {
            fetchUserData(userId);
        } else {
            Toast.makeText(getActivity(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Logout Button Click Listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + userId);

        // Fetch user data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        if (name != null) {
                            userName.setText(name);
                            userNameLabel.setText(name);
                            break;  // Stop after finding the first user
                        }
                    }
                } else {
                    Log.w("Firebase", "No users found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read user name", error.toException());
            }
        });

        return view;
    }

    private void fetchUserData(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name != null) {
                        userName.setText(name);
                        userNameLabel.setText(name);
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

    private void logoutUser() {
        // Clear user session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to Welcome Screen
        Intent intent = new Intent(requireActivity(), Welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears backstack
        startActivity(intent);

        // Close the MainActivity
        requireActivity().finish();
    }

    private void toggleEditMode() {
        if (!isEditing) {
            // Enable input fields for editing
            toggleEditable(true);
            editSaveButton.setText("Press to Save");
            editSaveButton.setTextColor(getResources().getColor(android.R.color.holo_purple));
        } else {
            // Disable input fields and save changes
            toggleEditable(false);
            editSaveButton.setText("Press to Edit");
            editSaveButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

            // You can save the updated values to a database or SharedPreferences here
            String updatedName = userName.getText().toString();
            String updatedInterests = userInterests.getText().toString();

            // Update user data in Firebase Realtime Database
            databaseReference.child("name").setValue(updatedName);
            databaseReference.child("interest").setValue(updatedInterests);

            // Update userNameLabel with the new name
            userNameLabel.setText(updatedName);
        }
        isEditing = !isEditing;  // Toggle the flag
    }

    private void toggleEditable(boolean enabled) {
        userName.setEnabled(enabled);

        if (!enabled) {
            // Disable user interaction with dropdown
            userInterests.setEnabled(false);
            userInterests.setFocusable(false);
            userInterests.setFocusableInTouchMode(false);
            userInterests.setClickable(false);
            userInterests.dismissDropDown();  // Close the dropdown if open
        } else {
            // Re-enable dropdown functionality
            userInterests.setEnabled(true);
            userInterests.setFocusable(true);
            userInterests.setFocusableInTouchMode(true);
            userInterests.setClickable(true);
        }
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}