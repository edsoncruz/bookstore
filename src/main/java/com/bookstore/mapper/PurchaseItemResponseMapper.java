package com.bookstore.mapper;

import com.bookstore.dto.PurchaseItemRequestDTO;
import com.bookstore.dto.PurchaseItemResponseDTO;
import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseItemResponseMapper extends BaseMapper<PurchaseItem, PurchaseItemResponseDTO> {

}
