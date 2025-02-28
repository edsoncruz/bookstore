package com.bookstore.repository;

import com.bookstore.entity.Customer;
import com.bookstore.entity.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyRepository extends JpaRepository<Loyalty, Long> {

    Loyalty findByCustomerId(Long customerId);
}
