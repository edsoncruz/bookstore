package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.CustomerRequestDTO;
import com.bookstore.dto.CustomerResponseDTO;
import com.bookstore.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController implements BaseController<CustomerRequestDTO, CustomerResponseDTO> {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO customerRequestDTO) {
        return this.customerService.create(customerRequestDTO) ;
    }

    @Override
    public CustomerResponseDTO update(CustomerRequestDTO customerRequestDTO) {
        return this.customerService.update(customerRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.customerService.delete(id);

        return new ResponseEntity<>(String.format("Customer id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public CustomerResponseDTO find(Long id) {
        return this.customerService.find(id);
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        return this.customerService.findAll();
    }
}
