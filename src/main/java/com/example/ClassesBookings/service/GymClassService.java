package com.example.ClassesBookings.service;

import com.example.ClassesBookings.model.ClassParams;
import com.example.ClassesBookings.model.GymClass;
import java.util.Date;
import java.util.List;


public interface GymClassService {

    public List<GymClass> getClasses(String name, String date);
    public List<GymClass> findClasses(String name, String date);
    public List<GymClass> postClasses(ClassParams params, Date parsedStartDate, Date parsedEndDate);

    public boolean isOverlappingClass(Date parsedStartDate, Date parsedEndDate);
    public List<GymClass> deleteClass(GymClass gymClass);

    public boolean validClassParams(ClassParams classParams);



}
