package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record PurchaseResponseDTO(
    Long customerId,
    List<PurchaseItemResponseDTO> purchaseItems,
    BigDecimal totalPrice
) implements BaseDTO{}
