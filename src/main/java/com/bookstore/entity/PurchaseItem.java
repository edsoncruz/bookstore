package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class PurchaseItem extends BaseEntity {

    @ManyToOne
    private Purchase purchase;

    @ManyToOne
    private Book book;

    private BigDecimal price;

    private Long amount;

    private LocalDateTime dateCreated;
}
