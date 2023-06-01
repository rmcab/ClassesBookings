package com.example.ClassesBookings.service;

import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.repository.GymClassRepository;
import com.example.ClassesBookings.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GymClassService {

    @Autowired
    private GymClassRepository repo;


    /*--------------------------------------------------gets-------------------------------------------*/
    public List<GymClass> findClasses(String name, String date){
        if(Utils.isNotNullOrEmptyString(name) || Utils.isNotNullOrEmptyString(date))
            return repo.findClasses(name, date);
        else
            return repo.getAllClasses();
    }
}
