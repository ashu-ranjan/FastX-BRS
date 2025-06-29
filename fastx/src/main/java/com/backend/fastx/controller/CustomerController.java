package com.backend.fastx.controller;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Customer;
import com.backend.fastx.service.CustomerService;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;


@RestController // takes responsibility for handling HTTP requests and responses
@RequestMapping("/fastx/api/customer") // base URL for all customer-related endpoints
@CrossOrigin(origins = "http://localhost:5173") // allows cross-origin requests from the specified origin
// This is useful for development when the frontend and backend are served from different origins.
public class CustomerController {

               
    @Autowired // automatically injects the CustomerService bean(object) into this controller
    private CustomerService customerService;

    /**
     * @aim add a new customer
     * @path /fastx/api/customer/add
     * @method POST
     * @description This method will add a new customer to the system. It will take the
     * @param customer
     * @return ResponseEntity with created Customer
     * @throws ResourceNotFoundException if customer already exists
     */
    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(customer));
    }

    /**
     * @aim update customer details
     * @path /fastx/api/customer/update 
     * @method PUT
     * @description Update customer details by email
     * @return ResponseEntity with updated Customer
     * @throws ResourceNotFoundException if customer not found
     */
    @PutMapping("/update/profile")
    public ResponseEntity<?> updateCustomer(Principal principal, // 
                                            @RequestBody Customer customer) {
        String email = principal.getName();
        Customer updatedCustomer = customerService.updateCustomer(email, customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * @aim upload customer image
     * @description This method will upload an image for the customer. It will take the customer
     * ID and the image file as parameters.
     * @param customerId The ID of the customer for whom the image is being uploaded.
     * @param file The image file to be uploaded.
     * @return ResponseEntity with success message
     * @throws IOException if an error occurs during file upload
     */

    @PostMapping("/upload/image")
    public String uploadCustomerImage(Principal principal, @RequestParam("file") MultipartFile file) throws IOException{
        String email = principal.getName();
        return customerService.uploadCustomerImage(email, file);
    }

    /**
     * @aim get current customer details
     * @description This method will retrieve the details of a customer by their email.
     * @param email The email of the customer whose details are to be retrieved.
     * @return ResponseEntity with Customer details
     * @throws ResourceNotFoundException if customer not found
     */
    @GetMapping("/get/profile")
    public ResponseEntity<?> getCurrentCustomer(Principal principal) {
        String email = principal.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

}
