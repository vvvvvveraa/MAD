package com.sp.project13;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Submit_newAct extends AppCompatActivity {

    private TextView actNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_new);

        // Find the TextView by its ID
        actNameTextView = findViewById(R.id.actName);

        // Retrieve the activity name from the Intent
        String activityName = getIntent().getStringExtra("activityName");

        // Set the activity name to the TextView
        if (activityName != null) {
            actNameTextView.setText(activityName);
        }

        // Find the button by its ID
        Button backButton = findViewById(R.id.b2home1);

        // Set an OnClickListener on the button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start UserMain activity
                Intent intent = new Intent(Submit_newAct.this, UserMain.class);
                // Optionally, you can add flags to clear the activity stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity
                finish();
            }
        });
    }
}