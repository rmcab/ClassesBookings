package com.example.ClassesBookings.controller;
import com.example.ClassesBookings.model.GymClass;
import com.example.ClassesBookings.service.GymClassService;
import com.example.ClassesBookings.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;

@RestController
@RequestMapping("api/classes")
public class GymClassController {

    @Autowired
    private GymClassService service;

    @Operation(summary = "Searches existing classes with optional parameters"
            ) /*notes = "Returns a list of classes"*/
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })

    @GetMapping
    public List<GymClass> getClasses(@RequestParam(name="name",required=false) String name, @RequestParam(name="date",required=false) String date){

        /*if(Utils.isNotNullOrEmptyString(date) && !Utils.isValidDate(date)){

        }*/

        return service.findClasses(name, date);
    }

}
