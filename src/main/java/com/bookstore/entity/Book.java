package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {
    private String title;
    private BigDecimal price;
    private Long amount;
    private LocalDateTime dateCreated;

    @Transient
    private BigDecimal priceSingle;

    @Transient
    private BigDecimal priceBundle;

    @ManyToOne
    private BookType bookType;

    public BigDecimal getPriceSingle() {
        BigDecimal discount = price.multiply(bookType.getDiscountOverall());
        this.priceSingle = price.subtract(discount);
        return priceSingle;
    }

    public BigDecimal getPriceBundle() {

        if(bookType.getMinimalBundle() == null){
            return null;
        }

        BigDecimal priceSingle = getPriceSingle();
        BigDecimal discount = priceSingle.multiply(bookType.getDiscountBundle());
        this.priceBundle =  priceSingle.subtract(discount);

        return this.priceBundle;
    }

    public BigDecimal getPriceForSell(Long amountToSell){
        if(bookType.getMinimalBundle() == null || amountToSell < bookType.getMinimalBundle()){
            return getPriceSingle();
        }else {
            return getPriceBundle();
        }
    }
}
