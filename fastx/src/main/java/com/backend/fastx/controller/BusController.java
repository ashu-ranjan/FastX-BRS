package com.backend.fastx.controller;

import com.backend.fastx.dto.BusAmenitiesDTO;
import com.backend.fastx.enums.BusType;
import com.backend.fastx.model.Bus;
import com.backend.fastx.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/fastx/api/bus")
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin as needed
public class BusController {

    @Autowired
    private BusService busService;

    /*
     * AIM: adding bus to db
     * PATH: /fastx/api/bus/add
     * METHOD: POST
     * RESPONSE: Bus
     * Authority: OPERATOR
     * */
    @PostMapping("/add")
    public ResponseEntity<?> addBus(@RequestBody Bus bus, Principal principal){
        String username = principal.getName();
        Bus addedBus = busService.addBus(bus, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBus);
    }

    /**
     * AIM: Get all bus for a given operator
     * PATH: /fastx/api/bus/get-buses
     * METHOD: GET
     * RESPONSE: List<Bus>
     * 
     */
    @GetMapping("/get-buses")
    public ResponseEntity<?> getBusesByOperator(Principal principal) {
        String username = principal.getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(busService.getBusesByOperator(username));
    }

    /**
     * AIM: fetch bus type
     * PATH: /fastx/api/bus/get-bus-type
     * METHOD: GET  
     * RESPONSE: List<String>
     * 
     */
    @GetMapping("/get-bus-type")
    public ResponseEntity<?> getBusTypes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BusType.values());
    }

    /*
     * AIM: add or update amenities for a bus
     * PATH: /fastx/api/bus/add-amenities
     * METHOD: POST
     * RESPONSE: BusAmenitiesDTO
     * Authority: OPERATOR
     * 
     */
    @PostMapping("/add-amenities")
    public ResponseEntity<?> addAmenitiesToBus(@RequestBody BusAmenitiesDTO amenitiesDTO, Principal principal) {
        String username = principal.getName();
        Bus updatedBus = busService.addAmenitiesToBus(amenitiesDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBus);
    }

    /*
     * AIM: get amenities for a bus
     * PATH: /fastx/api/bus/get-amenities
     * METHOD: GET
     * RESPONSE: BusAmenitiesDTO
     * Authority: OPERATOR, EXECUTIVE
     */
    @GetMapping("/get-amenities")
    public ResponseEntity<?> getAmenitiesForBus(Principal principal) {
        String username = principal.getName();
        BusAmenitiesDTO amenities = busService.getAmenitiesForBus(username);
        return ResponseEntity.status(HttpStatus.OK).body(amenities);
    }
}
