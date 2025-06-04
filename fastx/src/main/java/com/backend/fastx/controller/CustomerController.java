package com.backend.fastx.controller;

import com.backend.fastx.model.Customer;
import com.backend.fastx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fastx/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
     * AIM: adding customer to db
     * PATH: /fastx/api/customer/add
     * METHOD: POST
     * RESPONSE: Customer
     * */
    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(customer));
    }
}
