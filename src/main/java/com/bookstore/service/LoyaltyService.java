package com.bookstore.service;

import com.bookstore.entity.BookType;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Loyalty;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.LoyaltyRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class LoyaltyService implements BaseService<Loyalty> {

    public static final long BOOK_PRICE_WITH_POINTS = 10L;

    private final LoyaltyRepository loyaltyRepository;
    private final CustomerService customerService;

    public LoyaltyService(LoyaltyRepository loyaltyRepository, CustomerService customerService){
        this.loyaltyRepository = loyaltyRepository;
        this.customerService = customerService;
    }

    @Override
    public Loyalty create(Loyalty loyalty) {
        commonValidation(loyalty);
        requiredNullOrEmpty(loyalty.getId(), "Id must be null");

        if(loyaltyRepository.findByCustomerId(loyalty.getCustomer().getId()) != null)
            throw new BadRequestException(String.format("Customer id %s already exists", loyalty.getCustomer().getId()));

        return loyaltyRepository.save(loyalty);
    }

    @Override
    public Loyalty update(Loyalty loyalty) {
        commonValidation(loyalty);
        requiredValue(loyalty.getId(), "Id is required");

        return this.loyaltyRepository.save(loyalty);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.loyaltyRepository.deleteById(id);
    }

    @Override
    public Loyalty find(Long id) {
        Optional<Loyalty> bookOptional = this.loyaltyRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("Loyalty id %s not found", id));
        }
    }

    @Override
    public List<Loyalty> findAll() {
        return this.loyaltyRepository.findAll();
    }

    private void commonValidation(Loyalty loyalty){
        requiredValue(loyalty, "Loyalty is required");
        requiredValue(loyalty.getCustomer(), "Customer is required");
        igualOrGreaterThan(loyalty.getPoints(), 0L, "Points can't be negative");
    }

    public Loyalty incrementPoints(Long customerId, Long newPoints) {

        Loyalty loyalty = loyaltyRepository.findByCustomerId(customerId);

        if(loyalty == null){
            Customer customer = customerService.find(customerId);

            loyalty = new Loyalty();
            loyalty.setCustomer(customer);
            loyalty.setPoints(newPoints);

            return create(loyalty);
        }else {
            loyalty.addPoints(newPoints);

            return update(loyalty);
        }
    }

    public void paymentWithPoints(Long customerId, BookType bookType) {
        Loyalty loyalty = loyaltyRepository.findByCustomerId(customerId);

        requiredValue(loyalty, "Customer id %s doesn't have any point");
        requiredTrue(bookType.isPaybleWithPoints(), String.format("BookType %s is not payble with points", bookType.getName()));
        igualOrGreaterThan(loyalty.getPoints(), BOOK_PRICE_WITH_POINTS,
                String.format("Customer id %s doesn't have %s points to buy with points: %s", customerId, BOOK_PRICE_WITH_POINTS, loyalty.getPoints()));

        loyalty.setPoints(loyalty.getPoints() - BOOK_PRICE_WITH_POINTS);
        update(loyalty);
    }
}
