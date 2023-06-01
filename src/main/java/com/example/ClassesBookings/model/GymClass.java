package com.example.ClassesBookings.model;

import java.util.ArrayList;

public class GymClass{
    private String name;
    private String date;
    private int capacity;


    private ArrayList<Member> bookings;

    public GymClass(){

    }

    public GymClass(String name, String date, int capacity){
        this.name = name;
        this.date = date;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Member> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Member> bookings) {
        this.bookings = bookings;
    }


}
