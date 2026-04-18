package com.example.myapplication;

import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;

public class Rating {
    private int ratingID;
    private double stars;
    private String comment;
    private String date;
    private User customer;
    private Vendor vendor;

    // Constructor
    public Rating(double stars, String comment, String date, User customer, Vendor vendor) {
        this.stars = stars;
        this.comment = comment;
        this.date = date;
        this.customer = customer;
        this.vendor = vendor;
    }

    // Getters
    public double getStars() { return stars; }
    public String getComment() { return comment; }
    public String getDate() { return date; }
    public User getCustomer() { return customer; }
    public Vendor getVendor() { return vendor; }

    // ID
    public int getRatingID() { return ratingID; }
    public void setRatingID(int ratingID) { this.ratingID = ratingID; }
}
