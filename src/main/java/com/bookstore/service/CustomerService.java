package com.bookstore.service;

import com.bookstore.entity.Customer;
import com.bookstore.entity.User;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.requiredNullOrEmpty;
import static com.bookstore.helper.ValidationHelper.requiredValue;

@Service
public class CustomerService implements BaseService<Customer> {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    public CustomerService(CustomerRepository customerRepository, UserService userService){
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    @Override
    public Customer create(Customer customer) {
        commonValidation(customer);
        requiredNullOrEmpty(customer.getId(), "Id must be null");

        User user = userService.find(customer.getUser().getId());
        customer.setUser(user);

        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        commonValidation(customer);
        requiredValue(customer.getId(), "Id is required");

        return this.customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.customerRepository.deleteById(id);
    }

    @Override
    public Customer find(Long id) {
        Optional<Customer> bookOptional = this.customerRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("Customer id %s not found", id));
        }
    }

    @Override
    public List<Customer> findAll() {
        return this.customerRepository.findAll();
    }

    private void commonValidation(Customer customer){
        requiredValue(customer, "Customer is required");
        requiredValue(customer.getName(), "Name is required");
        requiredValue(customer.getUser(), "User is required");
        requiredValue(customer.getUser().getId(), "User id is required");
    }
}
