package com.example.ClassesBookings.controller;

import com.example.ClassesBookings.model.BookingParams;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private GymClassService serviceClass;

    @Autowired
    private BookingService service;

    @Operation(summary = "Creates a booking in a given date (dd-MM-yyyy) for existing classes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PostMapping
    public ResponseEntity<List<GymClass>> setBooking(@RequestBody BookingParams bookingParams) {

        try {
            String name = bookingParams.getName();
            String date = bookingParams.getDate();
            List<GymClass> list = serviceClass.findClasses(null, date);
            validateBookingsRequests(name, date, list);

            GymClass classWBookingToAdd = list.get(0).getBookings() != null && list.get(0).getBookings().stream().anyMatch(elem -> elem.equalsIgnoreCase(name)) ? null : list.get(0);
            if (classWBookingToAdd == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Couldn't create booking. Member is already booked in for " + date) ;
            }

            return new ResponseEntity<>(service.createBooking(name, classWBookingToAdd), HttpStatus.OK);

        }
        catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Utils.GENERIC_ERROR_MESSAGE);
            }
        }

    }

    @Operation(summary = "Deletes a booking of a member in a given date (dd-MM-yyyy) for existing classes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping
    public ResponseEntity<List<GymClass>> deleteBooking(@RequestBody BookingParams bookingParams) {

        try {
            String name = bookingParams.getName();
            String date = bookingParams.getDate();
            List<GymClass> list = serviceClass.findClasses(null, date);
            validateBookingsRequests(name, date, list);

            GymClass classWBookingToRemove = list.get(0).getBookings() != null && list.get(0).getBookings().stream().anyMatch(elem -> elem.equalsIgnoreCase(name)) ? list.get(0) : null;
            if (classWBookingToRemove == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found - No Bookings found for " + name + "on" + date);
            }

            return new ResponseEntity<>(service.deleteBooking(name, classWBookingToRemove), HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Utils.GENERIC_ERROR_MESSAGE);
            }
        }
    }

    private void validateBookingsRequests(String name, String date, List<GymClass> list){
        if (!Utils.isNotNullOrEmptyString(name) || !Utils.isNotNullOrEmptyString(date)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid Input Format - Member name and date of booking are mandatory");
        }

        if (Utils.getParsedDate(date) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid Date Format - Expected format: dd-MM-yyyy");
        }
        if (list == null || list.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Not Found - No Classes found for " + date);
        }
    }


}
