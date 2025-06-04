package com.backend.fastx.controller;

import com.backend.fastx.model.BusOperator;
import com.backend.fastx.service.BusOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fastx/api/bus-operator")
public class BusOperatorController {

    @Autowired
    private BusOperatorService busOperatorService;

    /*
     * AIM: adding bus-operator to db
     * PATH: /fastx/api/bus-operator/add
     * METHOD: POST
     * RESPONSE: BusOperator
     * */
    @PostMapping("/add")
    public ResponseEntity<?> addBusOperator(@RequestBody BusOperator busOperator){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(busOperatorService.addBusOperator(busOperator));
    }
}
