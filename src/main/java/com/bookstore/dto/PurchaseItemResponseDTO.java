package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import com.bookstore.entity.PurchaseItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

public record PurchaseItemResponseDTO(
    String bookTitle,
    BigDecimal priceUnit,
    Long amount,
    BigDecimal priceTotal
) implements BaseDTO{}
