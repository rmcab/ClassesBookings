package com.example.ClassesBookings.service;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.repository.GymClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private GymClassRepository repo;

    public List<GymClass> createBooking(String name, GymClass gymClass){
        return repo.updateClass(gymClass, setBooking(gymClass, name));
    }

    public List<GymClass> deleteBooking(String name, GymClass gymClass){
        return repo.updateClass(gymClass, removeBooking(gymClass, name));
    }

    private GymClass setBooking(GymClass gymClass, String name){
        gymClass.getBookings().add(name);
        return gymClass;
    }

    private GymClass removeBooking(GymClass gymClass, String name){
        gymClass.getBookings().remove(name);
        return gymClass;
    }
}
