package com.backend.fastx.controller;

import com.backend.fastx.model.BusRoute;
import com.backend.fastx.service.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fastx/api/bus-route")
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
}
