package com.bookstore.mapper;

import com.bookstore.dto.CustomerResponseDTO;
import com.bookstore.entity.Customer;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper extends BaseMapper<Customer, CustomerResponseDTO> {

}
