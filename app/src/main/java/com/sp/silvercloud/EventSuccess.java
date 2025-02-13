    package com.sp.silvercloud;

    import static android.content.ContentValues.TAG;

    import androidx.appcompat.app.AppCompatActivity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.Toast;
    import android.provider.CalendarContract;
    import java.util.TimeZone;
    import android.util.Log;  // Add this import for logging

    public class EventSuccess extends AppCompatActivity {
        private Button calendarBtn, homeBtn;

        private static final int REQUEST_CALENDAR_PERMISSION = 100;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_eventsuccess);


            // Get event data from the intent
            Intent intent = getIntent();
            String eventTitle = intent.getStringExtra("event_title");
            long eventStartTime = intent.getLongExtra("event_start_time", 0);
            long eventEndTime = intent.getLongExtra("event_end_time", 0);
            String eventDescription = intent.getStringExtra("event_description");

            // Log the received data for debugging purposes
            Log.d("EventSuccess", "Event Title: " + eventTitle);
            Log.d("EventSuccess", "Event Start Time: " + eventStartTime);
            Log.d("EventSuccess", "Event End Time: " + eventEndTime);
            Log.d("EventSuccess", "Event Description: " + eventDescription);

            // Set up the calendar button
            calendarBtn = findViewById(R.id.addCalendarButton);
            calendarBtn.setOnClickListener(v -> addEventToCalendar(eventTitle, eventStartTime, eventEndTime));

            // Set up the home button
            homeBtn = findViewById(R.id.backHomeButton);
            homeBtn.setOnClickListener(v -> {
                // Navigate to HomeFragment (or another activity)
                Intent homeIntent = new Intent(EventSuccess.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            });
        }

        private void addEventToCalendar(String eventTitle, long eventStartTime, long eventEndTime) {
            // Log the data that will be passed to the calendar intent
            Log.d("EventSuccess", "Adding event to calendar:");
            Log.d("EventSuccess", "Title: " + eventTitle);
            Log.d("EventSuccess", "Start Time: " + eventStartTime);
            Log.d("EventSuccess", "End Time: " + eventEndTime);
            Log.d("EventSuccess", "TimeZone: " + TimeZone.getDefault().getID());

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
    }
