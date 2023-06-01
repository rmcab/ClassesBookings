package com.example.ClassesBookings.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class GymClass{

    @Schema(name = "name", type = "string", format="string", description = "The Class Name", required = true, example = "Pilates")
    @JsonProperty("name")
    private String name;
    @Schema(name = "date", type = "string", format="string",  description = "The Class Date", required = true, example = "06/01/2023")
    @JsonProperty("date")
    private String date;

    @Schema(name = "capacity", type = "int", format="double", description = "The Class Capacity", required = true, example = "20")
    @JsonProperty("capacity")
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
