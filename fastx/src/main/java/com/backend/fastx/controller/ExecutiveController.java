package com.backend.fastx.controller;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Executive;
import com.backend.fastx.service.ExecutiveService;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fastx/api/executive")
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin as needed
public class ExecutiveController {

    @Autowired
    private ExecutiveService executiveService;

    /**
     * @aim add a new executive
     * @path /fastx/api/executive/add 
     * @method POST
     * @description Add a new executive to the system
     * @return ResponseEntity with created Executive
     */
    @PostMapping("/add")
    public ResponseEntity<?> addExecutive(@RequestBody Executive executive){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(executiveService.addExecutive(executive));
    }

    /**
     * @aim update executive details
     * @path /fastx/api/executive/update 
     * @method PUT
     * @description Update executive details by email
     * @return ResponseEntity with updated Executive
     * @throws ResourceNotFoundException if executive not found
     */
    @PutMapping("/update/profile")
    public ResponseEntity<?> updateExecutive(Principal principal, @RequestBody Executive executive){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(executiveService.updateExecutive(principal.getName(), executive));
    }

    /**
     * @aim upload executive image
     * @description This method will upload an image for the executive. It will take the executive
     * @param executiveId
     * @param file
     * @return String URL of the uploaded image
     * @methodName uploadExecutiveImage
     * @throws IOException
     */

    @PostMapping("/upload/image")
     public String uploadExecutiveImage(Principal principal, @RequestParam("file") MultipartFile file) throws IOException {
            return executiveService.uploadExecutiveImage(principal.getName(), file);
     }

    /**
     * @aim get executive profile
     * @path /fastx/api/executive/get/profile
     * @method GET
     * @description Get the profile information of the logged-in executive
     * @return ResponseEntity with Executive profile information
     */
    @GetMapping("/get/profile")
    public ResponseEntity<?> getExecutiveProfile(Principal principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(executiveService.getExecutiveProfile(principal.getName()));
    }
}
