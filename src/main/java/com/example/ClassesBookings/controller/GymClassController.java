package com.example.ClassesBookings.controller;

import com.example.ClassesBookings.model.ClassParams;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.GymClassService;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/classes")
public class GymClassController {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected error has occurred. Please try again later";

    @Autowired
    private GymClassService service;

    @Operation(summary = "Gets classes with optional search parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
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

    @Operation(summary = "Posts classes, use same start and end date to create a single class or a date interval to create multiple, subsequent classes. There can only be one class per day"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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

            if (service.isOverlappingClass(parsedStartDate, parsedEndDate)) {
                return new ResponseEntity<>(
                        "Invalid Date Interval - There's one or more classes already registered in the given date interval",
                        HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(service.postClasses(classParams, parsedStartDate, parsedEndDate), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deletes an existing class on a given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteClass(@RequestParam(name = "date", required = true) String date) {

        try {
            if (!Utils.isNotNullOrEmptyString(date) || Utils.getParsedDate(date) == null) {
                String response = "Invalid Input Format - Date is Mandatory";
                if (!Utils.isNotNullOrEmptyString(date))
                    response = "Invalid Date Format - Expected format: dd-MM-yyyy";
                return new ResponseEntity<>(
                        response,
                        HttpStatus.BAD_REQUEST);
            }

            List<GymClass> listToDelete = service.findClasses(null, date);
            if (listToDelete == null || listToDelete.size() == 0) {
                return new ResponseEntity<>(
                        "Not Found - No Classes found for " + date,
                        HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(service.deleteClass(listToDelete.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    GENERIC_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
