package com.sp.project13;

public class Event {
    private String title;
    private String eventCode;
    private String date;
    private String filter;
    private String fullDescription;
    private String imageUrl;
    private String organizerId;
    private String newDescription; // This is the new field
    private String imageId; // New field for image ID

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String title, String eventCode, String date, String filter, String fullDescription, String imageUrl, String organizerId, String newDescription, String imageId) {
        this.title = title;
        this.eventCode = eventCode;
        this.date = date;
        this.filter = filter;
        this.fullDescription = fullDescription;
        this.imageUrl = imageUrl;
        this.organizerId = organizerId;
        this.newDescription = newDescription; // Initialize the new field
        this.imageId = imageId; // Initialize the new field
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getNewDescription() {
        return newDescription; // Getter for newDescription
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription; // Setter for newDescription
    }

    // Getter and Setter for imageId
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}