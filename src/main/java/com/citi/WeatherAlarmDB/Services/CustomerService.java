package com.citi.WeatherAlarmDB.Services;

import com.citi.WeatherAlarmDB.Models.Customer;
import com.citi.WeatherAlarmDB.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer(String uuid) {
        Customer customer = new Customer();
        customer.setUuid(uuid);
        customer.setIsPremium(false);

        return customerRepository.save(customer);
    }
}
