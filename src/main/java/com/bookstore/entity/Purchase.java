package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class Purchase extends BaseEntity {

    @ManyToOne
    private Customer customer;

    private LocalDateTime dateCreated;

    private BigDecimal priceOrderTotal;

    public void addPriceOrderTotal(BigDecimal priceToAdd, Long multiplier){
        if(this.priceOrderTotal == null){
            this.priceOrderTotal = BigDecimal.ZERO;
        }
        this.priceOrderTotal = this.priceOrderTotal.add(priceToAdd.multiply(new BigDecimal(multiplier)));
    }
}
