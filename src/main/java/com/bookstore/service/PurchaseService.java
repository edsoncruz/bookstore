package com.bookstore.service;

import com.bookstore.dto.BookRequestDTO;
import com.bookstore.dto.PurchaseRequestDTO;
import com.bookstore.dto.PurchaseResponseDTO;
import com.bookstore.entity.*;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.PurchaseRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class PurchaseService implements BaseService<Purchase> {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemService purchaseItemService;
    private final BookService bookService;
    private final CustomerService customerService;
    private final LoyaltyService loyaltyService;
    private final UserService userService;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemService purchaseItemService, BookService bookService, CustomerService customerService, LoyaltyService loyaltyService, UserService userService){
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemService = purchaseItemService;
        this.bookService = bookService;
        this.customerService = customerService;
        this.loyaltyService = loyaltyService;
        this.userService = userService;
    }

    @Override
    public Purchase create(Purchase purchase) {
        commonValidation(purchase);
        requiredNullOrEmpty(purchase.getId(), "Id must be null");
        purchase.setDateCreated(LocalDateTime.now());

        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase update(Purchase purchase) {
        commonValidation(purchase);
        requiredValue(purchase.getId(), "Id is required");

        return this.purchaseRepository.save(purchase);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.purchaseRepository.deleteById(id);
    }

    @Override
    public Purchase find(Long id) {
        Optional<Purchase> bookOptional = this.purchaseRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("Purchase id %s not found", id));
        }
    }

    @Override
    public List<Purchase> findAll() {
        return this.purchaseRepository.findAll();
    }

    private void commonValidation(Purchase purchase){
        requiredValue(purchase, "Purchase is required");
        requiredValue(purchase.getCustomer(), "Customer is required");
        positiveValue(purchase.getPriceOrderTotal(), "TotalPrice must be positive");
    }

    @Transactional
    public PurchaseResponseDTO order(PurchaseRequestDTO purchaseRequestDTO){

        Customer customer = customerService.find(purchaseRequestDTO.getCustomerId());

        userService.validateAuthorization(customer);

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase = create(purchase);

        Long pointsLoyalty = 0L;

        for(BookRequestDTO bookDTO: purchaseRequestDTO.getBooks()){
            greaterThan(bookDTO.getAmount(), 0L, "Book amount must be at least 1");

            Book book = bookService.find(bookDTO.getId());

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setBook(book);
            purchaseItem.setPurchase(purchase);
            purchaseItem.setAmount(bookDTO.getAmount());

            if (bookDTO.isPaymentWithPoints()) {
                loyaltyService.paymentWithPoints(customer.getId(), book.getBookType());
                purchaseItem.setPrice(BigDecimal.ZERO);
            }else {
                purchaseItem.setPrice(book.getPriceForSell(bookDTO.getAmount()));
            }

            purchaseItemService.create(purchaseItem);
            bookService.decreaseAmount(book.getId(), bookDTO.getAmount());

            purchase.addPriceOrderTotal(purchaseItem.getPrice(), purchaseItem.getAmount());
            pointsLoyalty += purchaseItem.getAmount();
        }

        purchase = update(purchase);
        loyaltyService.incrementPoints(customer.getId(), pointsLoyalty);

        List<PurchaseItem> purchaseItems = purchaseItemService.findByPurchase(purchase);

        return new PurchaseResponseDTO(purchase, purchaseItems);
    }
}
