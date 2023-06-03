package com.example.ClassesBookings.controller;

import com.example.ClassesBookings.model.ClassParams;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.GymClassServiceImpl;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/classes")
public class GymClassController {

    @Autowired
    private GymClassServiceImpl service;

    @Operation(summary = "Gets classes with optional search parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @GetMapping
    public ResponseEntity<List<GymClass>> getClasses(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "date", required = false) String date) {

        try {
            if (Utils.isNotNullOrEmptyString(date) && Utils.getParsedDate(date) == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Date Format - Expected format: dd-MM-yyyy");
            }

            return new ResponseEntity<>(service.getClasses(name, date), HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Utils.GENERIC_ERROR_MESSAGE);
            }
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
    public ResponseEntity<List<GymClass>> putClasses(@RequestBody ClassParams classParams) {

        try {

            if (!service.validClassParams(classParams)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Input Format - Class name, capacity, start and end date are mandatory;" +
                        " Expected format for dates: dd-MM-yyyy");
            }

            Date parsedStartDate = Utils.getParsedDate(classParams.getStartDate());
            Date parsedEndDate = Utils.getParsedDate(classParams.getEndDate());

            if (parsedStartDate.after(parsedEndDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Date - Start Date must be previous to End Date - Expected format: dd-MM-yyyy");
            }

            if (service.isOverlappingClass(parsedStartDate, parsedEndDate)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Date Interval - There's one or more classes already registered in the given date interval");
            }

            return new ResponseEntity<>(service.postClasses(classParams, parsedStartDate, parsedEndDate), HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Utils.GENERIC_ERROR_MESSAGE);
            }
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
    public ResponseEntity<List<GymClass>> deleteClass(@RequestBody String date) {

        try {
            if (!Utils.isNotNullOrEmptyString(date) || Utils.getParsedDate(date) == null) {
                String response = "Invalid Input Format - Date is Mandatory";
                if (Utils.getParsedDate(date) == null)
                    response = "Invalid Date Format - Expected format: dd-MM-yyyy";
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,response);
            }

            List<GymClass> listToDelete = service.findClasses(null, date);
            if (listToDelete == null || listToDelete.size() == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Not Found - No Classes found for " + date);
            }

            return new ResponseEntity<>(service.deleteClass(listToDelete.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw (ResponseStatusException) e;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Utils.GENERIC_ERROR_MESSAGE);
            }
        }
    }

}
