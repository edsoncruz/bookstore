package com.bookstore.service;

import com.bookstore.dto.CustomerRequestDTO;
import com.bookstore.dto.CustomerResponseDTO;
import com.bookstore.entity.Customer;
import com.bookstore.entity.User;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.CustomerRequestMapper;
import com.bookstore.mapper.CustomerResponseMapper;
import com.bookstore.mapper.UserResponseMapper;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.requiredNullOrEmpty;
import static com.bookstore.helper.ValidationHelper.requiredValue;

@Service
public class CustomerService implements BaseService<CustomerRequestDTO, CustomerResponseDTO> {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;
    private final UserResponseMapper userResponseMapper;

    public CustomerService(CustomerRepository customerRepository, UserService userService, CustomerRequestMapper customerRequestMapper, CustomerResponseMapper customerResponseMapper, UserResponseMapper userResponseMapper){
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.customerRequestMapper = customerRequestMapper;
        this.customerResponseMapper = customerResponseMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @Override
    public CustomerResponseDTO create(CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRequestMapper.toEntity(customerRequestDTO);

        commonValidation(customer);
        requiredNullOrEmpty(customer.getId(), "Id must be null");

        User user = userResponseMapper.toEntity(userService.find(customer.getUser().getId()));
        customer.setUser(user);

        return customerResponseMapper.toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerResponseDTO update(CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRequestMapper.toEntity(customerRequestDTO);

        commonValidation(customer);
        requiredValue(customer.getId(), "Id is required");

        return customerResponseMapper.toDTO(customerRepository.save(customer));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.customerRepository.deleteById(id);
    }

    @Override
    public CustomerResponseDTO find(Long id) {
        return this.customerRepository
                .findById(id)
                .map(customerResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("Customer id %s not found", id)));
    }

    @Override
    public List<CustomerResponseDTO> findAll() {
        return this.customerRepository
                .findAll()
                .stream()
                .map(customerResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(Customer customer){
        requiredValue(customer, "Customer is required");
        requiredValue(customer.getName(), "Name is required");
        requiredValue(customer.getUser(), "User is required");
        requiredValue(customer.getUser().getId(), "User id is required");
    }
}
