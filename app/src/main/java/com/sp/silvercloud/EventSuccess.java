package com.sp.silvercloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.provider.CalendarContract;
import java.util.TimeZone;

public class EventSuccess extends AppCompatActivity {
    private Button calendarBtn, homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventsuccess);

        calendarBtn = findViewById(R.id.addCalendarButton);
        calendarBtn.setOnClickListener(onCal);

        homeBtn = findViewById(R.id.backHomeButton);
        homeBtn.setOnClickListener(onHome);
    }

    View.OnClickListener onCal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Example: Event details (replace with actual Firebase data)
            String eventTitle = "Sample Event";  // Replace with Firebase data
            long eventStartTime = System.currentTimeMillis() + 86400000; // Example: Tomorrow
            long eventEndTime = eventStartTime + 3600000; // Example: +1 hour

            // Create an intent to insert the event into the calendar
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, eventTitle);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime);
            intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            // Check if a calendar app is available
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(EventSuccess.this, "No calendar app found", Toast.LENGTH_SHORT).show();
            }
        }
    };


    View.OnClickListener onHome = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate to SecondActivity
            Intent intent = new Intent(EventSuccess.this, HomeFragment.class);
            startActivity(intent);

            // To close current Activity class and exit
            finish();
        }
    };
}