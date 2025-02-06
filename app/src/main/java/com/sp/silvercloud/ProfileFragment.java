package com.sp.silvercloud;

import android.content.Intent;
import android.text.InputType;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

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
    private TextView editSaveButton;
    private EditText userName;
    private AutoCompleteTextView userInterests;
    private Button logout;
    private boolean isEditing = false;  // Flag to track edit mode

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.nameField);
        userInterests = view.findViewById(R.id.interestsField);
        editSaveButton = view.findViewById(R.id.editSaveButton);

        // Initialize the button properly
        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(onLogout);

        // Initially lock the fields
        toggleEditable(false);

        // Set button click listener
        editSaveButton.setOnClickListener(v -> toggleEditMode());

        return view;
    }

    private void toggleEditMode() {
        if (!isEditing) {
            // Enable input fields for editing
            toggleEditable(true);
            editSaveButton.setText("Press to Save");
        } else {
            // Disable input fields and save changes
            toggleEditable(false);
            editSaveButton.setText("Press to Edit");

            // You can save the updated values to a database or SharedPreferences here
            String updatedName = userName.getText().toString();
            String updatedInterests = userInterests.getText().toString();
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

    View.OnClickListener onLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to Welcome activity
            Intent intent = new Intent(getActivity(), Welcome.class);
            startActivity(intent);

            // Finish the parent activity to exit
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    };
}