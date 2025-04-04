package com.bookstore.service;

import com.bookstore.dto.BookResponseDTO;
import com.bookstore.dto.PurchaseRequestDTO;
import com.bookstore.dto.PurchaseResponseDTO;
import com.bookstore.dto.PurchaseBookRequestDTO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Purchase;
import com.bookstore.entity.PurchaseItem;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.*;
import com.bookstore.repository.PurchaseRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class PurchaseService implements BaseService<PurchaseRequestDTO, PurchaseResponseDTO> {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemService purchaseItemService;
    private final PurchaseRequestMapper purchaseRequestMapper;
    private final PurchaseResponseMapper purchaseResponseMapper;
    private final BookService bookService;
    private final CustomerService customerService;
    private final LoyaltyService loyaltyService;
    private final UserService userService;
    private final CustomerResponseMapper customerResponseMapper;
    private final BookResponseMapper bookResponseMapper;
    private final PurchaseItemRequestMapper purchaseItemRequestMapper;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemService purchaseItemService, PurchaseRequestMapper purchaseRequestMapper, PurchaseResponseMapper purchaseResponseMapper, BookService bookService, CustomerService customerService, LoyaltyService loyaltyService, UserService userService, CustomerResponseMapper customerResponseMapper, BookResponseMapper bookResponseMapper, PurchaseItemRequestMapper purchaseItemRequestMapper){
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemService = purchaseItemService;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.purchaseResponseMapper = purchaseResponseMapper;
        this.bookService = bookService;
        this.customerService = customerService;
        this.loyaltyService = loyaltyService;
        this.userService = userService;
        this.customerResponseMapper = customerResponseMapper;
        this.bookResponseMapper = bookResponseMapper;
        this.purchaseItemRequestMapper = purchaseItemRequestMapper;
    }

    @Override
    public PurchaseResponseDTO create(PurchaseRequestDTO purchaseRequestDTO) {
        Purchase purchase = purchaseRequestMapper.toEntity(purchaseRequestDTO);

        commonValidation(purchase);
        requiredNullOrEmpty(purchase.getId(), "Id must be null");
        purchase.setDateCreated(LocalDateTime.now());

        return purchaseResponseMapper.toDTO(purchaseRepository.save(purchase));
    }

    @Override
    public PurchaseResponseDTO update(PurchaseRequestDTO purchaseRequestDTO) {
        Purchase purchase = purchaseRequestMapper.toEntity(purchaseRequestDTO);

        commonValidation(purchase);
        requiredValue(purchase.getId(), "Id is required");

        return purchaseResponseMapper.toDTO(this.purchaseRepository.save(purchase));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.purchaseRepository.deleteById(id);
    }

    @Override
    public PurchaseResponseDTO find(Long id) {
        return this.purchaseRepository
                .findById(id)
                .map(purchaseResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("Purchase id %s not found", id)));
    }

    @Override
    public List<PurchaseResponseDTO> findAll() {
        return this.purchaseRepository.
                findAll()
                .stream()
                .map(purchaseResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(Purchase purchase){
        requiredValue(purchase, "Purchase is required");
        requiredValue(purchase.getCustomer(), "Customer is required");
        positiveValue(purchase.getPriceOrderTotal(), "TotalPrice must be positive");
    }

    @Transactional
    public PurchaseResponseDTO order(PurchaseRequestDTO purchaseRequestDTO){

        Customer customer = customerResponseMapper.toEntity(customerService.find(purchaseRequestDTO.customerId()));

        userService.validateAuthorization(customer);

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase = purchaseResponseMapper.toEntity(create(purchaseRequestMapper.toDTO(purchase)));

        Long pointsLoyalty = 0L;

        for(PurchaseBookRequestDTO bookDTO: purchaseRequestDTO.books()){
            greaterThan(bookDTO.amount(), 0L, "Book amount must be at least 1");

            Book book = bookResponseMapper.toEntity(bookService.find(bookDTO.id()));

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setBook(book);
            purchaseItem.setPurchase(purchase);
            purchaseItem.setAmount(bookDTO.amount());

            if (bookDTO.isPaymentWithPoints()) {
                loyaltyService.paymentWithPoints(customer.getId(), book.getBookType());
                purchaseItem.setPrice(BigDecimal.ZERO);
            }else {
                purchaseItem.setPrice(book.getPriceForSell(bookDTO.amount()));
            }

            purchaseItemService.create(purchaseItemRequestMapper.toDTO(purchaseItem));
            bookService.decreaseAmount(book.getId(), bookDTO.amount());

            purchase.addPriceOrderTotal(purchaseItem.getPrice(), purchaseItem.getAmount());
            pointsLoyalty += purchaseItem.getAmount();
        }

        purchase = purchaseResponseMapper.toEntity(update(purchaseRequestMapper.toDTO(purchase)));
        loyaltyService.incrementPoints(customer.getId(), pointsLoyalty);

        List<PurchaseItem> purchaseItems = purchaseItemService.findByPurchase(purchase);

        return purchaseResponseMapper.toDTO(purchase, purchaseItems);
    }
}
