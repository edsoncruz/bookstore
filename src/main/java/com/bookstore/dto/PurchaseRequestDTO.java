package com.bookstore.dto;

import com.bookstore.entity.Purchase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PurchaseRequestDTO {
    private Long customerId;
    private List<BookRequestDTO> books;
}
