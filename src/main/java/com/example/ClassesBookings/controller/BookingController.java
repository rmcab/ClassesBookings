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
            List<GymClass> list = serviceClass.findClasses(null, date);

            ResponseEntity<Object> res= validateBookingsRequests(name, date, list);

            if(res != null){
                return res;
            }

            GymClass classWBookingToAdd = list.get(0).getBookings() != null && list.get(0).getBookings().contains(name) ? null : list.get(0);
            if (classWBookingToAdd == null) {
                return new ResponseEntity<>(
                        "Couldn't create booking. Member is already booked in for " + date,
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(service.createBooking(name, classWBookingToAdd), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deletes a booking of a member in a given date for existing classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteBooking(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "date", required = true) String date) {

        try {
            List<GymClass> list = serviceClass.findClasses(null, date);

            ResponseEntity<Object> res= validateBookingsRequests(name, date, list);

            if(res != null){
                return res;
            }

            GymClass classWBookingToRemove = list.get(0).getBookings() != null && list.get(0).getBookings().contains(name) ? list.get(0) : null;
            if (classWBookingToRemove == null) {
                return new ResponseEntity<>(
                        "Not Found - No Bookings found for " + name + "on" + date,
                        HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(service.deleteBooking(name, classWBookingToRemove), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> validateBookingsRequests(String name, String date, List<GymClass> list){
        if (!Utils.isNotNullOrEmptyString(name) || !Utils.isNotNullOrEmptyString(date)) {
            return new ResponseEntity<>(
                    "Invalid Input Format - Member name and date of booking are mandatory",
                    HttpStatus.BAD_REQUEST);
        }

        if (Utils.getParsedDate(date) == null) {
            return new ResponseEntity<>(
                    "Invalid Date Format - Expected format: dd-MM-yyyy",
                    HttpStatus.BAD_REQUEST);
        }
        if (list == null || list.size() == 0) {
            return new ResponseEntity<>(
                    "Not Found - No Classes found for " + date,
                    HttpStatus.NOT_FOUND);
        }
        return null;

    }


}
