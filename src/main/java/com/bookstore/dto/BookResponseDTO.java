package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record BookResponseDTO(
    Long id,
    String title,
    BigDecimal price,
    Long amount,
    LocalDateTime dateCreated,
    BigDecimal priceSingle,
    BigDecimal priceBundle,
    BookTypeResponseDTO bookType
) implements BaseDTO {}
