package com.sp.silvercloud;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventItem implements Serializable {  // Make sure it implements Serializable

    private String date;
    private String eventCode;
    private String eventId;
    private String fullDescription;
    private String imageUrl;
    private String interest;
    private String newDescription;
    private String title;
    private String time;  // Add a field for time, for example "4pm to 6pm"
    private String category; // Indoor or Outdoor

    // Default constructor for Firebase
    public EventItem() {
    }

    // Constructor to initialize an EventItem
    public EventItem(String date, String eventCode, String eventId, String fullDescription, String imageUrl,
                     String interest, String newDescription, String title, String time) {
        this.date = date;
        this.eventCode = eventCode;
        this.eventId = eventId;
        this.fullDescription = fullDescription;
        this.imageUrl = imageUrl;
        this.interest = interest;
        this.newDescription = newDescription;
        this.title = title;
        this.time = time;
        this.category = category;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }


    // Method to parse the event time (e.g., "4pm to 6pm") and return the start and end time in milliseconds
    // Method to parse the event time (e.g., "10 am" or "3 pm") and return the start and end time in milliseconds
    public long[] parseEventTime() {
        long[] eventTimes = new long[2];
        try {
            // Split the time range (e.g., "10:00am to 11:00am")
            String[] times = this.time.split(" to ");
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");  // Pattern for "10:00 am", "1:00 pm"

            // Create calendar instance for the event date
            Calendar calendar = Calendar.getInstance();

            // Extract start and end times
            String startTime = times[0].trim();  // Example: "10:00am"
            String endTime = times[1].trim();    // Example: "11:00am"

            // Fix any missing space between time and "am/pm" if necessary
            startTime = startTime.replaceAll("(\\d{1,2}:\\d{2})([ap]m)", "$1 $2");
            endTime = endTime.replaceAll("(\\d{1,2}:\\d{2})([ap]m)", "$1 $2");

            // Debugging: Print the formatted times
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);

            // Parse start time
            Date startDate = sdf.parse(startTime);  // Parse "10:00 am" or "1:00 pm"
            calendar.setTime(startDate);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.date.split(" ")[0]));  // Use the day of the month
            calendar.set(Calendar.MONTH, getMonthIndex(this.date.split(" ")[1]));  // Get the month index (e.g., "Feb" -> 1)
            calendar.set(Calendar.YEAR, Integer.parseInt(this.date.split(" ")[2]));  // Use the year

            eventTimes[0] = calendar.getTimeInMillis();  // Start time in milliseconds

            // Debugging: Print out the start time in milliseconds
            System.out.println("Start Time in Millis: " + eventTimes[0]);

            // Parse end time
            Date endDate = sdf.parse(endTime);  // Parse "10:00 am" or "1:00 pm"
            calendar.setTime(endDate);

            // Set the same date for the end time (same day as start)
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.date.split(" ")[0]));  // Ensure same day
            calendar.set(Calendar.MONTH, getMonthIndex(this.date.split(" ")[1]));  // Ensure same month
            calendar.set(Calendar.YEAR, Integer.parseInt(this.date.split(" ")[2]));  // Ensure same year

            eventTimes[1] = calendar.getTimeInMillis();  // End time in milliseconds

            // Debugging: Print out the end time in milliseconds
            System.out.println("End Time in Millis: " + eventTimes[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventTimes;
    }



    // Helper method to convert month abbreviation (e.g., "Feb") to month index (e.g., 1 for February)
    private int getMonthIndex(String month) {
        switch (month.toLowerCase()) {
            case "jan": return 0;
            case "feb": return 1;
            case "mar": return 2;
            case "apr": return 3;
            case "may": return 4;
            case "jun": return 5;
            case "jul": return 6;
            case "aug": return 7;
            case "sep": return 8;
            case "oct": return 9;
            case "nov": return 10;
            case "dec": return 11;
            default: return -1;
        }
    }
}
