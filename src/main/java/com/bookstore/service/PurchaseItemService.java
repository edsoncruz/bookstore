package com.bookstore.service;

import com.bookstore.dto.PurchaseItemRequestDTO;
import com.bookstore.dto.PurchaseItemResponseDTO;
import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.PurchaseItemRequestMapper;
import com.bookstore.mapper.PurchaseItemResponseMapper;
import com.bookstore.repository.PurchaseItemRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.requiredNullOrEmpty;
import static com.bookstore.helper.ValidationHelper.requiredValue;

@Service
public class PurchaseItemService implements BaseService<PurchaseItemRequestDTO, PurchaseItemResponseDTO> {

    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseItemRequestMapper purchaseItemRequestMapper;
    private final PurchaseItemResponseMapper purchaseItemResponseMapper;

    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository, PurchaseItemRequestMapper purchaseItemRequestMapper, PurchaseItemResponseMapper purchaseItemResponseMapper){
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseItemRequestMapper = purchaseItemRequestMapper;
        this.purchaseItemResponseMapper = purchaseItemResponseMapper;
    }

    @Override
    public PurchaseItemResponseDTO create(PurchaseItemRequestDTO purchaseItemRequestDTO) {
        PurchaseItem purchaseItem = purchaseItemRequestMapper.toEntity(purchaseItemRequestDTO);

        commonValidation(purchaseItem);
        requiredNullOrEmpty(purchaseItem.getId(), "Id must be null");

        purchaseItem.setDateCreated(LocalDateTime.now());

        return purchaseItemResponseMapper.toDTO(purchaseItemRepository.save(purchaseItem));
    }

    @Override
    public PurchaseItemResponseDTO update(PurchaseItemRequestDTO purchaseItemRequestDTO) {
        PurchaseItem purchaseItem = purchaseItemRequestMapper.toEntity(purchaseItemRequestDTO);

        commonValidation(purchaseItem);
        requiredValue(purchaseItem.getId(), "Id is required");

        return purchaseItemResponseMapper.toDTO(this.purchaseItemRepository.save(purchaseItem));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.purchaseItemRepository.deleteById(id);
    }

    @Override
    public PurchaseItemResponseDTO find(Long id) {
        return this.purchaseItemRepository
                .findById(id)
                .map(purchaseItemResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("PurchaseItem id %s not found", id)));
    }

    @Override
    public List<PurchaseItemResponseDTO> findAll() {
        return this.purchaseItemRepository
                .findAll()
                .stream()
                .map(purchaseItemResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(PurchaseItem purchaseItem){
        requiredValue(purchaseItem, "PurchaseItem is required");
        requiredValue(purchaseItem.getBook(), "Book is required");
        requiredValue(purchaseItem.getPrice(), "Price is required");
    }

    public List<PurchaseItem> findByPurchase(Purchase purchase){
        return this.purchaseItemRepository.findByPurchase(purchase);
    }
}
