package com.example.ClassesBookings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class BookingParams {

    @Schema(name = "name", type = "string",  description = "The member name", required = true, example = "John Doe")
    @JsonProperty("name")
    private String name;

    @Schema(name = "date", type = "string",  description = "The booking date (dd-MM-yyyy)", required = true, example = "01-07-2023")
    @JsonProperty("date")
    private String date;

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
}
