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

    public List<GymClass> removeClass(GymClass toDeleteClass) {
        this.classes.remove(toDeleteClass);
        return getAllClasses();
    }

    public List<GymClass> updateClass(GymClass existing, GymClass toUpdate){
        int index = this.classes.indexOf(existing);
        this.classes.set(index, toUpdate);
        return getAllClasses();
    }

}
