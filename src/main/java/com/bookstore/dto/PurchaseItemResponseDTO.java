package com.bookstore.dto;

import com.bookstore.entity.Book;
import com.bookstore.entity.PurchaseItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class PurchaseItemResponseDTO {
    private String bookTitle;
    private BigDecimal priceUnit;
    private Long amount;
    private BigDecimal priceTotal;

    public PurchaseItemResponseDTO(PurchaseItem purchaseItem){
        this.bookTitle = purchaseItem.getBook().getTitle();
        this.priceUnit = purchaseItem.getPrice();
        this.amount = purchaseItem.getAmount();
        this.priceTotal = purchaseItem.getPrice().multiply(new BigDecimal(purchaseItem.getAmount()));
    }
}
