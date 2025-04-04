package com.bookstore.mapper;

import com.bookstore.dto.PurchaseRequestDTO;
import com.bookstore.dto.PurchaseResponseDTO;
import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseResponseMapper extends BaseMapper<Purchase, PurchaseResponseDTO> {

    PurchaseResponseDTO toDTO(Purchase entity, List<PurchaseItem> purchaseItems);
}
