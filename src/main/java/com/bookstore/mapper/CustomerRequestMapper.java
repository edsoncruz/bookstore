package com.bookstore.mapper;

import com.bookstore.dto.CustomerRequestDTO;
import com.bookstore.entity.Customer;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper extends BaseMapper<Customer, CustomerRequestDTO> {

}
