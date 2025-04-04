package com.bookstore.mapper;

import com.bookstore.dto.BookRequestDTO;
import com.bookstore.dto.PurchaseRequestDTO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Purchase;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper extends BaseMapper<Purchase, PurchaseRequestDTO> {

}
