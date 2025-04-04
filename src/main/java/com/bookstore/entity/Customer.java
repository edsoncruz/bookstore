package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne
    private User user;
}