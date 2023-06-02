package com.example.ClassesBookings.controller;

import com.example.ClassesBookings.model.ClassParams;
import com.example.ClassesBookings.service.GymClassService;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;

@RestController
@RequestMapping("/classes")
public class GymClassController {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occured. Please try again later";


    @Autowired
    private GymClassService service;

    @Operation(summary = "Gets classes with optional search parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request")
    })

    @GetMapping
    public ResponseEntity<Object> getClasses(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "date", required = false) String date) {


        try {
            if (Utils.isNotNullOrEmptyString(date) && Utils.getParsedDate(date) == null) {
                return new ResponseEntity<>(
                        "Invalid Date Format - Expected format: dd-MM-yyyy",
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(service.getClasses(name, date), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Object> putClasses(@RequestBody ClassParams classParams) {

        try {

            if (!service.validClassParams(classParams)) {
                return new ResponseEntity<>(
                        "Invalid Input Format - Class name, capacity, start and end date are mandatory;" +
                                " Expected format for dates: dd-MM-yyyy",
                        HttpStatus.BAD_REQUEST);
            }

            Date parsedStartDate = Utils.getParsedDate(classParams.getStartDate());
            Date parsedEndDate = Utils.getParsedDate(classParams.getEndDate());

            if (parsedStartDate.after(parsedEndDate)) {
                return new ResponseEntity<>(
                        "Invalid Date - Start Date must be previous to End Date - Expected format: dd-MM-yyyy",
                        HttpStatus.BAD_REQUEST);
            }

            if(service.isOverlappingClass(parsedStartDate, parsedEndDate)){
                return new ResponseEntity<>(
                        "Invalid Date Interval - There's one or more classes already registered in the given date interval",
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(service.postClasses(classParams, parsedStartDate, parsedEndDate), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
