package com.example.ClassesBookings.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isNotNullOrEmptyString(String s){
        return s != null && !"".equals(s);
    }

    public static boolean isValidDate (String dateString){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("mm/dd/yyyy");
        dateFormatter.setLenient(false);
        Date date = null;
        try{
            date = dateFormatter.parse(dateString);
        } catch(Exception e){

        }
        return date != null;
    }
}
