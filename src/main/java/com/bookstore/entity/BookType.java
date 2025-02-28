package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class BookType extends BaseEntity {
    private String name;
    private BigDecimal discountOverall;
    private Integer minimalBundle;
    private BigDecimal discountBundle;
    private Boolean payableWithPoints;

    public Boolean isPaybleWithPoints(){
        return payableWithPoints;
    }
}
