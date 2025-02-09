package com.sp.silvercloud;

import java.io.Serializable;

public class UserDetails implements Serializable {  // Make sure it implements Serializable
    private String email;
    private String interest;
    private String password;

    // Default constructor for Firebase
    public UserDetails() {
    }

    // Constructor to initialize an EventItem
    public UserDetails(String email, String interest, String password) {
        this.email = email;
        this.interest = interest;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
