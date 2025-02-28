package com.bookstore.service;

import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.PurchaseItemRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.requiredNullOrEmpty;
import static com.bookstore.helper.ValidationHelper.requiredValue;

@Service
public class PurchaseItemService implements BaseService<PurchaseItem> {

    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository){
        this.purchaseItemRepository = purchaseItemRepository;
    }

    @Override
    public PurchaseItem create(PurchaseItem purchaseItem) {
        commonValidation(purchaseItem);
        requiredNullOrEmpty(purchaseItem.getId(), "Id must be null");

        purchaseItem.setDateCreated(LocalDateTime.now());

        return purchaseItemRepository.save(purchaseItem);
    }

    @Override
    public PurchaseItem update(PurchaseItem purchaseItem) {
        commonValidation(purchaseItem);
        requiredValue(purchaseItem.getId(), "Id is required");

        return this.purchaseItemRepository.save(purchaseItem);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.purchaseItemRepository.deleteById(id);
    }

    @Override
    public PurchaseItem find(Long id) {
        Optional<PurchaseItem> bookOptional = this.purchaseItemRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("PurchaseItem id %s not found", id));
        }
    }

    @Override
    public List<PurchaseItem> findAll() {
        return this.purchaseItemRepository.findAll();
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
