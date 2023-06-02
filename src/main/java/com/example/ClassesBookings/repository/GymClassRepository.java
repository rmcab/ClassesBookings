package com.example.ClassesBookings.repository;

import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.utils.Utils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GymClassRepository {
    private List<GymClass> classes = new ArrayList<>();

    public List<GymClass> getAllClasses(){
        return this.classes;
    }

    public void createClass(GymClass newClass){
        this.classes.add(newClass);
    }

}
