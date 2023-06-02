package com.example.ClassesBookings.controller;

import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.BookingService;
import com.example.ClassesBookings.service.GymClassService;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occurred. Please try again later";

    @Autowired
    private GymClassService serviceClass;

    @Autowired
    private BookingService service;

    @Operation(summary = "Creates a booking in a given date for existing classes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PostMapping
    public ResponseEntity<Object> setBooking(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "date", required = true) String date) {

        try {

            if  (!Utils.isNotNullOrEmptyString(name) || !Utils.isNotNullOrEmptyString(date)){
                return new ResponseEntity<>(
                        "Invalid Input Format - Member name and date of booking are mandatory",
                        HttpStatus.BAD_REQUEST);
            }

            if(Utils.getParsedDate(date) == null){
                return new ResponseEntity<>(
                        "Invalid Date Format - Expected format: dd-MM-yyyy",
                        HttpStatus.BAD_REQUEST);
            }

            List<GymClass> listToAdd = serviceClass.findClasses(null, date);
            if (listToAdd == null || listToAdd.size() == 0) {
                return new ResponseEntity<>(
                        "Not Found - No Classes found for " + date,
                        HttpStatus.NOT_FOUND);
            }

            if(listToAdd.stream().filter(elem -> elem.getBookings().contains(name)).findAny().orElse(null) != null){
                return new ResponseEntity<>(
                        "Couldn't create booking. Member is already booked in for " + date,
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(service.createBooking(name, listToAdd), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
