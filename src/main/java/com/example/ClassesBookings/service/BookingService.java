package com.example.ClassesBookings.service;

import com.example.ClassesBookings.model.GymClass;

import java.util.List;

public interface BookingService {

    public List<GymClass> createBooking(String name, GymClass gymClass);
    public List<GymClass> deleteBooking(String name, GymClass gymClass);
}
