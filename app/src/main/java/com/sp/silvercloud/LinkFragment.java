package com.sp.silvercloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LinkFragment extends Fragment {

    private ImageView ActiveSG;
    private ImageView HealthHub;
    private ImageView Healthy365;
    private ImageView LifeSG;
    private ImageView myENV;
    private ImageView myResponder;

    public LinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_link, container, false);

        // Initialize ImageViews
        ActiveSG = view.findViewById(R.id.ActiveSG);
        HealthHub = view.findViewById(R.id.HealthHub);
        Healthy365 = view.findViewById(R.id.Health365);
        LifeSG = view.findViewById(R.id.LifeSG);
        myENV = view.findViewById(R.id.myENV);
        myResponder = view.findViewById(R.id.myResponder);

        // Set click listeners for each ImageView
        ActiveSG.setOnClickListener(v -> gotoUrl("https://activesg.gov.sg/home"));
        HealthHub.setOnClickListener(v -> gotoUrl("https://www.healthhub.sg/"));
        Healthy365.setOnClickListener(v -> gotoUrl("https://www.healthhub.sg/programmes/healthyliving"));
        LifeSG.setOnClickListener(v -> gotoUrl("https://www.life.gov.sg/"));
        myENV.setOnClickListener(v -> gotoUrl("https://www.nea.gov.sg/"));
        myResponder.setOnClickListener(v -> gotoUrl("https://www.scdf.gov.sg/home/community---volunteers/community-resources/myresponder-app"));

        return view;
    }

    private void gotoUrl(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}