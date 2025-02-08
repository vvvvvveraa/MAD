package com.sp.project13;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class biz_currentAct extends AppCompatActivity {

    private ImageView eventImage;
    private TextView textTitle, textDescription, textDate, textCode;

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
    }
}