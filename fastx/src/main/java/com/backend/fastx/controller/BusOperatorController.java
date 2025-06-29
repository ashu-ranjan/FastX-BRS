package com.backend.fastx.controller;

import com.backend.fastx.model.BusOperator;
import com.backend.fastx.service.BusOperatorService;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fastx/api/bus-operator")
@CrossOrigin(origins = "http://localhost:5173")
public class BusOperatorController {

    @Autowired
    private BusOperatorService busOperatorService;

    /**
     * @aim add a new bus operator
     * @description This method will add a new bus operator to the system. It will take the bus operator details as a parameter.
     * @methodName addBusOperator
     * @path /fastx/api/bus-operator/add
     * @method POST
     * @param busOperator
     * @return ResponseEntity with created BusOperator
     */
    @PostMapping("/add")
    public ResponseEntity<?> addBusOperator(@RequestBody BusOperator busOperator){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(busOperatorService.addBusOperator(busOperator));
    }

    /**
     * @aim update bus operator details
     * @path /fastx/api/bus-operator/update 
     * @method PUT
     * @description Update bus operator details by email
     * @return ResponseEntity with updated BusOperator
     * @throws ResourceNotFoundException if bus operator not found
     */

    @PutMapping("/update/profile")
    public ResponseEntity<?> updateBusOperator(Principal principal, @RequestBody BusOperator busOperator){
        String email = principal.getName();
        busOperator.setEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(busOperatorService.updateBusOperator(busOperator));
    }

    /**
     * @aim upload bus operator image
     * @description This method will upload an image for the bus operator. It will take the
     * @param busOperatorId
     * @param file
     * @return String URL of the uploaded image
     * @methodName uploadBusOperatorImage
     * @throws IOException
     * @throws java.io.IOException 
     */

    @PostMapping("/upload/image")
    public String uploadBusOperatorImage(Principal principal, @RequestParam("file") MultipartFile file) throws IOException{
        String email = principal.getName();
        return busOperatorService.uploadBusOperatorImage(email, file);
    }

    /**
     * @aim get bus operator profile
     * @description This method will retrieve the bus operator's profile using their email.
     * @return ResponseEntity with BusOperator details
     * @throws ResourceNotFoundException if bus operator not found
     * 
     */
    @GetMapping("/get/profile")
    public ResponseEntity<?> getBusOperatorProfile(Principal principal) {
        String email = principal.getName();
        BusOperator busOperator = busOperatorService.getBusOperatorByEmail(email);
        return ResponseEntity.ok(busOperator);
    }

}
