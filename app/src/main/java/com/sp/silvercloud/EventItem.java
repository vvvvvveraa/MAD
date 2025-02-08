package com.sp.silvercloud;

import java.io.Serializable;

public class EventItem implements Serializable {  // Make sure it implements Serializable

    private String date;
    private String eventCode;
    private String eventId;
    private String fullDescription;
    private String imageUrl;
    private String interest;
    private String newDescription;
    private String title;

    // Default constructor for Firebase
    public EventItem() {
    }

    // Constructor to initialize an EventItem
    public EventItem(String date, String eventCode, String eventId, String fullDescription, String imageUrl,
                     String interest, String newDescription, String title) {
        this.date = date;
        this.eventCode = eventCode;
        this.eventId = eventId;
        this.fullDescription = fullDescription;
        this.imageUrl = imageUrl;
        this.interest = interest;
        this.newDescription = newDescription;
        this.title = title;
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
}