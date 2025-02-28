package com.bookstore.dto;

import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
public class PurchaseResponseDTO {
    private Long customerId;
    private List<PurchaseItemResponseDTO> purchaseItems;
    private BigDecimal totalPrice;

    public PurchaseResponseDTO(Purchase purchase, List<PurchaseItem> purchaseItems){
        this.customerId = purchase.getCustomer().getId();
        this.purchaseItems = new ArrayList<>();
        purchaseItems.forEach(purchaseItem -> {
            PurchaseItemResponseDTO purchaseItemResponseDTO = new PurchaseItemResponseDTO(purchaseItem);
            this.purchaseItems.add(purchaseItemResponseDTO);
        });
        this.totalPrice = purchase.getPriceOrderTotal();
    }
}
