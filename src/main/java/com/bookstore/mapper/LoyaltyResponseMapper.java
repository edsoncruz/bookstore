package com.bookstore.mapper;

import com.bookstore.dto.LoyaltyResponseDTO;
import com.bookstore.entity.Loyalty;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoyaltyResponseMapper extends BaseMapper<Loyalty, LoyaltyResponseDTO> {

}
