package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public record PurchaseRequestDTO(
    Long customerId,
    List<PurchaseBookRequestDTO> books
) implements BaseDTO {}
