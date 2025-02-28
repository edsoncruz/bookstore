package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class Loyalty extends BaseEntity {

    @ManyToOne
    private Customer customer;

    private Long points;

    public void addPoints(Long newPoints){
        if(this.points == null){
            this.points = 0L;
        }

        this.points += newPoints;
    }
}
