package com.backend.fastx.controller;

import com.backend.fastx.model.Bus;
import com.backend.fastx.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/fastx/api/bus")
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
}
