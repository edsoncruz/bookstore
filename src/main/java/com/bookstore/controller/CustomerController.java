package com.bookstore.controller;

import com.bookstore.entity.Customer;
import com.bookstore.controller.base.BaseController;
import com.bookstore.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController implements BaseController<Customer> {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @Override
    public Customer create(Customer customer) {
        return this.customerService.create(customer) ;
    }

    @Override
    public Customer update(Customer customer) {
        return this.customerService.update(customer);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.customerService.delete(id);

        return new ResponseEntity<>(String.format("Customer id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public Customer find(Long id) {
        return this.customerService.find(id);
    }

    @Override
    public List<Customer> findAll() {
        return this.customerService.findAll();
    }
}
