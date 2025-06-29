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

    /**
     * @aim Add a new bus route
     * @path /fastx/api/bus-route/add
     * @method POST
     * @description This method will add a new bus route to the system.
     *              It requires the bus route details in the request body.
     * @param busRoute
     * @return ResponseEntity<BusRoute>
     */

    @PostMapping("/add")
    public ResponseEntity<?> addRoute(@RequestBody BusRoute busRoute){
        busRoute = busRouteService.addRoute(busRoute);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(busRoute);
    }

    /**
     * @aim Get all bus routes
     * @description This method retrieves all bus routes from the system.
     *              It returns a list of BusRoute objects.
     * @path /fastx/api/bus-route/get-all
     * @method GET
     * @return ResponseEntity<List<BusRoute>>
     */

     @GetMapping("/get-all")
     public ResponseEntity<List<BusRoute>> getAllRoutes() {
         return ResponseEntity.ok(busRouteService.getAllRoutes());
     }
}
     
