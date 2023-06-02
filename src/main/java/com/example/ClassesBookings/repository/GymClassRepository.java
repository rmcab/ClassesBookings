package com.example.ClassesBookings.repository;
import com.example.ClassesBookings.model.GymClass;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GymClassRepository {
    private List<GymClass> classes = new ArrayList<>();

    public List<GymClass> getAllClasses() {
        return this.classes;
    }

    public void createClass(GymClass newClass) {
        this.classes.add(newClass);
    }

    public void removeClass(GymClass toDeleteClass) {
        this.classes.remove(toDeleteClass);
    }

    public void updateClass(GymClass existing, GymClass toUpdate){
        int index = this.classes.indexOf(existing);
        this.classes.set(index, toUpdate);
    }

}
