package com.example.ClassesBookings.controller;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.GymClassService;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;

@RestController
@RequestMapping("/classes")
public class GymClassController {

    @Autowired
    private GymClassService service;

    @Operation(summary = "Gets classes with optional search parameters"
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid request")
    })

    @GetMapping
    public ResponseEntity<Object> getClasses(@RequestParam(name="name",required=false) String name, @RequestParam(name="date",required=false) String date){

        if(Utils.isNotNullOrEmptyString(date) && !Utils.isValidDate(date)){
            return new ResponseEntity<>(
                    "Invalid Date Format - Expected format: MM/DD/YYYY",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.findClasses(name, date), HttpStatus.OK);
    }

}
