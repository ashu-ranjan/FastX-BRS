package com.backend.fastx.controller;

import com.backend.fastx.model.Executive;
import com.backend.fastx.service.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fastx/api/executive")
public class ExecutiveController {

    @Autowired
    private ExecutiveService executiveService;

    /*
     * AIM: adding executive to db
     * PATH: /fastx/api/executive/add
     * METHOD: POST
     * RESPONSE: Executive
     * */
    @PostMapping("/add")
    public ResponseEntity<?> addExecutive(@RequestBody Executive executive){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(executiveService.addExecutive(executive));
    }

}
