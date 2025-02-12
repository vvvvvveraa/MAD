package com.sp.silvercloud;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class biz_currentAct extends AppCompatActivity {

    private ImageView eventImage;
    private TextView textTitle, textDescription, textDate, textCode;
    private ImageButton imageButtonBack; // Declare the back button
    private Button btnDelete;
    private DatabaseReference rootDatabaseref;
    private String eventId; // Add this variable to store the eventId

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
        btnDelete = findViewById(R.id.buttonEventCompleted);

        // Initialize Firebase Database reference
        rootDatabaseref = FirebaseDatabase.getInstance().getReference("events");

        // Get the data from the Intent
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        String activityCode = getIntent().getStringExtra("activityCode");
        eventId = getIntent().getStringExtra("eventId"); // Retrieve the eventId

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

        // Set up the delete button click listener
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventId != null) {
                    Log.d("DeleteEvent", "Attempting to delete event with ID: " + eventId);
                    rootDatabaseref.child(eventId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(biz_currentAct.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after deletion
                        } else {
                            Toast.makeText(biz_currentAct.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(biz_currentAct.this, "Event ID is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}