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
import java.util.stream.Collectors;

@Service
public class GymClassServiceImpl implements GymClassService {

    @Autowired
    private GymClassRepository repo;


    public List<GymClass> getClasses(String name, String date) {
        if (Utils.isNotNullOrEmptyString(name) || Utils.isNotNullOrEmptyString(date))
            return findClasses(name, date);
        else
            return repo.getAllClasses();
    }

    public List<GymClass> findClasses(String name, String date) {
        List<GymClass> filteredClasses = repo.getAllClasses().stream().filter(elem -> filterClass(name, date, elem)).collect(Collectors.toList());
        return filteredClasses;
    }

    public List<GymClass> postClasses(ClassParams params, Date parsedStartDate, Date parsedEndDate) {

        Long daysBetween = Utils.getDaysBetweenDates(Utils.convertToLocalDateTime(parsedStartDate), Utils.convertToLocalDateTime(parsedEndDate)) + 1;

        for (int i = 0; i < daysBetween; i++) {
            GymClass gymClass = setGymClass(params, DateUtils.addDays(parsedStartDate, i));
            repo.createClass(gymClass);
        }

        return repo.getAllClasses();
    }

    public boolean isOverlappingClass(Date parsedStartDate, Date parsedEndDate) {
        GymClass overlappingCLass = repo.getAllClasses().stream().filter(elem -> Utils.isWithinDates(parsedStartDate, parsedEndDate, Utils.getParsedDate(elem.getDate()))).findAny().orElse(null);
        return overlappingCLass != null;
    }

    public List<GymClass> deleteClass(GymClass gymClass) {
        return repo.removeClass(gymClass);
    }

    public boolean validClassParams(ClassParams classParams) {
        return validClassBasicParams(classParams) && validClassDateParams(classParams);
    }

    private boolean validClassBasicParams(ClassParams classParams) {
        return Utils.isNotNullOrEmptyString(classParams.getName()) &&
                (classParams.getCapacity() != null && classParams.getCapacity() > 0);
    }

    private boolean validClassDateParams(ClassParams classParams) {
        return (Utils.isNotNullOrEmptyString(classParams.getStartDate()) && Utils.getParsedDate(classParams.getStartDate()) != null)
                && (Utils.isNotNullOrEmptyString(classParams.getEndDate()) && Utils.getParsedDate(classParams.getEndDate()) != null);
    }

    private GymClass setGymClass(ClassParams params, Date date) {
        GymClass gymClass = new GymClass();
        gymClass.setName(params.getName());
        gymClass.setCapacity(params.getCapacity());
        gymClass.setDate(Utils.getParsedStringFromDate(date));
        gymClass.setBookings(new ArrayList<>());
        return gymClass;
    }

    private boolean filterClass(String name, String date, GymClass elem) {
        if ((Utils.isNotNullOrEmptyString(name)) && (Utils.isNotNullOrEmptyString(date))) {
            return elem.getName().equalsIgnoreCase(name) && elem.getDate().equals(date);
        } else if (Utils.isNotNullOrEmptyString(name)) {
            return elem.getName().equalsIgnoreCase(name);
        } else {
            return elem.getDate().equals(date);
        }
    }


}
