package com.example.ClassesBookings.service;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.repository.GymClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private GymClassRepository repo;

    public List<GymClass> createBooking(String name, List<GymClass> listToAdd){
        listToAdd.forEach(gymClass -> repo.updateClass(gymClass, setBooking(gymClass, name)));
        return repo.getAllClasses();
    }

    private GymClass setBooking(GymClass gymClass, String name){
        gymClass.getBookings().add(name);
        return gymClass;
    }
}
