package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import com.bookstore.entity.Customer;

public record LoyaltyRequestDTO(
    Customer customer,
    Long points
) implements BaseDTO{}
