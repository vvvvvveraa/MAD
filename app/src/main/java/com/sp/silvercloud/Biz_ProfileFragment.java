package com.sp.silvercloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Ensure to import Button

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Biz_ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Biz_ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private Button button;
    private FirebaseUser  user;

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;

    public Biz_ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Biz_ProfileFragment.
     */
    public static Biz_ProfileFragment newInstance(String param1, String param2) {
        Biz_ProfileFragment fragment = new Biz_ProfileFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biz__profile, container, false);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser ();

        // Check if user is logged in
        if (user == null) {
            // If not logged in, redirect to Welcome activity
            Intent intent = new Intent(getActivity(), Welcome.class);
            startActivity(intent);
            // Optionally, you can call getActivity().finish() if you want to close this fragment
            // getActivity().finish();
        }

        // Initialize button
        button = view.findViewById(R.id.biz_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
                // Optionally, you can call getActivity().finish() if you want to close this fragment
                // getActivity().finish();
            }
        });

        return view;
    }
}