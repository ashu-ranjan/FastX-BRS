package com.backend.fastx.controller;

import com.backend.fastx.model.BusRoute;
import com.backend.fastx.service.BusRouteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/fastx/api/bus-route")
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin as needed
public class BusRouteController {

    @Autowired
    private BusRouteService busRouteService;

    /*
     * AIM: adding Bus Route to db
     * PATH: /fastx/api/bus-route/add
     * METHOD: POST
     * RESPONSE: BusRoute
     * AUTHORITY: EXECUTIVE only can add routes on which buses will operate
     * */

    /* **** For now i am granting all to add route **** */

    @PostMapping("/add")
    public ResponseEntity<?> addRoute(@RequestBody BusRoute busRoute){
        busRoute = busRouteService.addRoute(busRoute);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(busRoute);
    }

    /*
     * AIM: Get all bus routes
     * PATH: /fastx/api/bus-route/get-all
     * METHOD: GET  
     * RESPONSE: List<BusRoute>
     * AUTHORITY: EXECUTIVE or OPERATOR can get routes on which buses will operate
     * */

     @GetMapping("/get-all")
     public ResponseEntity<List<BusRoute>> getAllRoutes() {
         return ResponseEntity.ok(busRouteService.getAllRoutes());
     }
}
     
