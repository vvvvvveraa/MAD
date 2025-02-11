package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {
    private Button userButton;
    private Button organizerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Find the buttons by its IDs
        userButton = findViewById(R.id.user_button);
        organizerButton = findViewById(R.id.organizer_button);

        userButton.setOnClickListener(onUser);
        organizerButton.setOnClickListener(onOrganizer);
    }

    View.OnClickListener onUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(Welcome.this, UserLogin.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };

    View.OnClickListener onOrganizer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(Welcome.this, OrganizerLogin.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
}