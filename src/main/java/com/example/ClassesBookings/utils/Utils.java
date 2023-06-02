package com.example.ClassesBookings.utils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class Utils {

    public static boolean isNotNullOrEmptyString(String s){
        return s != null && !"".equals(s);
    }

    public static Date getParsedDate (String dateString){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dateFormatter.setLenient(false);
        Date date = null;
        try{
            date = dateFormatter.parse(dateString);
        } catch(Exception e){

        }
        return date;
    }

    public static String getParsedStringFromDate(Date date){
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return new java.sql.Timestamp(
                date.getTime()).toLocalDateTime();
    }

    public static Long getDaysBetweenDates(LocalDateTime startDate, LocalDateTime endDate){
        return Duration.between(startDate, endDate).toDays();
    }
}
