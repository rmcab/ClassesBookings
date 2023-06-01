package com.example.ClassesBookings.controller;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.GymClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/classes")
public class GymClassController {

    @Autowired
    private GymClassService service;

    @GetMapping
    public List<GymClass> getClasses(@RequestParam String name, @RequestParam String date){
        //needs to validate date format; If date format is invalid give error message;

        return service.findClasses(name, date);
    }

}
