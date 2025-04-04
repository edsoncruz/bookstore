package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


public record PurchaseBookRequestDTO(
    Long id,
    Long amount,
    Boolean paymentWithPoints
) implements BaseDTO{

    public Boolean isPaymentWithPoints(){
        if(paymentWithPoints == null)
            return false;

        return paymentWithPoints;
    }

    public Boolean getPaymentWithPoints() {
        return isPaymentWithPoints();
    }
}
