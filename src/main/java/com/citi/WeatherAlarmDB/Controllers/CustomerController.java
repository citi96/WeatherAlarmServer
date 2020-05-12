package com.citi.WeatherAlarmDB.Controllers;

import com.citi.WeatherAlarmDB.Models.Customer;
import com.citi.WeatherAlarmDB.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<Customer> addCustomer(@RequestParam String uuid) {
        Customer customer = customerService.addCustomer(uuid);

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
}