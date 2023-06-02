package com.example.ClassesBookings.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class GymClass{

    @Schema(name = "name", type = "string",  description = "The class name", required = true, example = "Pilates")
    @JsonProperty("name")
    private String name;
    @Schema(name = "date", type = "string",  description = "The class date", required = true, example = "06-01-2023")
    @JsonProperty("date")
    private String date;

    @Schema(name = "capacity", type = "int", description = "The class capacity", required = true, example = "20")
    @JsonProperty("capacity")
    private Integer capacity;

    @Schema(name = "bookings", type = "array",  description = "The class bookings", example = "['Michael']")
    @JsonProperty("bookings")
    private ArrayList<String> bookings;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ArrayList<String> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<String> bookings) {
        this.bookings = bookings;
    }


}
