package com.bookstore.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class BookRequestDTO {
    private Long id;
    private Long amount;
    private Boolean paymentWithPoints;

    public Boolean isPaymentWithPoints(){
        if(paymentWithPoints == null)
            paymentWithPoints = false;

        return paymentWithPoints;
    }

    public Boolean getPaymentWithPoints() {
        return isPaymentWithPoints();
    }
}
