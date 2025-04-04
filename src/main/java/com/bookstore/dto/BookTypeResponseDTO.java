package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;

import java.math.BigDecimal;

public record BookTypeResponseDTO(
    Long id,
    String name,
    BigDecimal discountOverall,
    Integer minimalBundle,
    BigDecimal discountBundle,
    Boolean payableWithPoints
) implements BaseDTO {}
