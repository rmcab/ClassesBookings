package com.example.ClassesBookings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class ClassParams {

    @Schema(name = "name", type = "string",  description = "The class name", required = true, example = "Pilates")
    @JsonProperty("name")
    private String name;
    @Schema(name = "startDate", type = "string",  description = "The class start date (dd-MM-yyyy)", required = true, example = "01-07-2023")
    @JsonProperty("startDate")
    private String startDate;

    @Schema(name = "endDate", type = "string",  description = "The class end date (dd-MM-yyyy)", required = true, example = "01-07-2023")
    @JsonProperty("endDate")
    private String endDate;

    @Schema(name = "capacity", type = "int", description = "The class capacity", required = true, example = "20")
    @JsonProperty("capacity")
    private Integer capacity;

    public ClassParams(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
