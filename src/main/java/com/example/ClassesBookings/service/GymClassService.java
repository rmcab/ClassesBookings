package com.example.ClassesBookings.service;

import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.model.ClassParams;
import com.example.ClassesBookings.repository.GymClassRepository;
import com.example.ClassesBookings.utils.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    /*-------------------------------------------------post---------------------------------------------*/
    //falta verificar se para aquele intervalo de datas já existe alguma aula;
    public List<GymClass> postClasses(ClassParams params, Date parsedStartDate, Date parsedEndDate){

        Long daysBetween = Utils.getDaysBetweenDates(Utils.convertToLocalDateTime(parsedStartDate), Utils.convertToLocalDateTime(parsedEndDate)) + 1 ;

        for(int i = 0; i < daysBetween; i++){
            GymClass gymClass = setGymClass(params, DateUtils.addDays(parsedStartDate, i));
            repo.getAllClasses().add(gymClass);
        }

        return repo.getAllClasses();
    }

    private GymClass setGymClass(ClassParams params, Date date){
        GymClass gymClass = new GymClass();
        gymClass.setName(params.getName());
        gymClass.setCapacity(params.getCapacity());
        gymClass.setDate(Utils.getParsedStringFromDate(date));
        gymClass.setBookings(new ArrayList<>());

        return gymClass;

    }











    /*--------------------------------------------------validations-----------------------------------*/
    public boolean validClassParams(ClassParams classParams){
        return validClassBasicParams(classParams) && validClassDateParams(classParams);
    }


    public boolean validClassBasicParams(ClassParams classParams){
        return Utils.isNotNullOrEmptyString(classParams.getName()) &&
                (classParams.getCapacity() != null && classParams.getCapacity() > 0);
    }

    public boolean validClassDateParams(ClassParams classParams){
        return (Utils.isNotNullOrEmptyString(classParams.getStartDate()) && Utils.getParsedDate(classParams.getStartDate()) != null)
                && (Utils.isNotNullOrEmptyString(classParams.getEndDate()) && Utils.getParsedDate(classParams.getEndDate()) != null);
    }


}
