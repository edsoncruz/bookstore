package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;

import java.math.BigDecimal;

public record PurchaseItemRequestDTO(
    String bookTitle,
    BigDecimal priceUnit,
    Long amount,
    BigDecimal priceTotal
) implements BaseDTO{}

 /*
    public PurchaseItemResponseDTO(PurchaseItem purchaseItem){
        this.bookTitle = purchaseItem.getBook().getTitle();
        this.priceUnit = purchaseItem.getPrice();
        this.amount = purchaseItem.getAmount();
        this.priceTotal = purchaseItem.getPrice().multiply(new BigDecimal(purchaseItem.getAmount()));
    }*/

