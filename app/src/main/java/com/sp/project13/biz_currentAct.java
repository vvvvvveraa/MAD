package com.sp.project13;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class biz_currentAct extends AppCompatActivity {

    private ImageView eventImage;
    private TextView textTitle, textDescription, textDate, textCode;
    private ImageButton imageButtonBack; // Declare the back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_current);

        // Initialize views
        eventImage = findViewById(R.id.eventImage);
        textTitle = findViewById(R.id.textTitle);
        textDescription = findViewById(R.id.textDescription);
        textDate = findViewById(R.id.textDate);
        textCode = findViewById(R.id.textCode);
        imageButtonBack = findViewById(R.id.imageButtonBack); // Initialize the back button

        // Get the data from the Intent
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        String activityCode = getIntent().getStringExtra("activityCode");

        // Set the data to the views
        textTitle.setText(title);
        textDescription.setText(description);
        textDate.setText(date);
        textCode.setText(activityCode);

        // Load the image using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(eventImage);
        }

        // Set up the back button click listener
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });
    }
}