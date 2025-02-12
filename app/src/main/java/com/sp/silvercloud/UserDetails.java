package com.sp.silvercloud;

import java.io.Serializable;

public class UserDetails implements Serializable {  // Make sure it implements Serializable
    private String email;
    private String password;
    private String name;
    private String interest;


    // Default constructor for Firebase
    public UserDetails() {
    }

    // Constructor to initialize an EventItem
    public UserDetails(String email, String password, String name, String interest) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.interest = interest;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
