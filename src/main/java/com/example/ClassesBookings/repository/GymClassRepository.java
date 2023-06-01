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


    /*-----------------------------------------------------------gets------------------------------------------------*/
    public List<GymClass> getAllClasses(){
        return this.classes;
    }
    public List<GymClass> findClasses(String name, String date){
        List<GymClass> filteredClasses = this.classes.stream().filter(elem -> filterClass(name, date, elem)).collect(Collectors.toList());
        return filteredClasses;
    }
    private boolean filterClass(String name, String date, GymClass elem){
        if( (Utils.isNotNullOrEmptyString(name)) && (Utils.isNotNullOrEmptyString(date)) ){
            return elem.getName().equals(name) && elem.getDate().equals(date);
        } else if(Utils.isNotNullOrEmptyString(name)){
            return elem.getName().equals(name);
        } else {
            return elem.getDate().equals(date);
        }
    }

    /*--------------------------------------------------------posts----------------------------------------------*/
    public List<GymClass> createClass(GymClass newClass){
        this.classes.add(newClass);
        return this.classes;
    }


    /*-----------------------------------------------------------UTILS-------------------------------------------*/



}
