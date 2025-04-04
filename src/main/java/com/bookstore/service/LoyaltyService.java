package com.bookstore.service;

import com.bookstore.dto.LoyaltyRequestDTO;
import com.bookstore.dto.LoyaltyResponseDTO;
import com.bookstore.entity.BookType;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Loyalty;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.CustomerResponseMapper;
import com.bookstore.mapper.LoyaltyRequestMapper;
import com.bookstore.mapper.LoyaltyResponseMapper;
import com.bookstore.repository.LoyaltyRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class LoyaltyService implements BaseService<LoyaltyRequestDTO, LoyaltyResponseDTO> {

    public static final long BOOK_PRICE_WITH_POINTS = 10L;

    private final LoyaltyRepository loyaltyRepository;
    private final CustomerService customerService;
    private final LoyaltyResponseMapper loyaltyResponseMapper;
    private final LoyaltyRequestMapper loyaltyRequestMapper;
    private final CustomerResponseMapper customerResponseMapper;

    public LoyaltyService(LoyaltyRepository loyaltyRepository, CustomerService customerService, LoyaltyResponseMapper loyaltyResponseMapper, LoyaltyRequestMapper loyaltyRequestMapper, CustomerResponseMapper customerResponseMapper){
        this.loyaltyRepository = loyaltyRepository;
        this.customerService = customerService;
        this.loyaltyResponseMapper = loyaltyResponseMapper;
        this.loyaltyRequestMapper = loyaltyRequestMapper;
        this.customerResponseMapper = customerResponseMapper;
    }

    @Override
    public LoyaltyResponseDTO create(LoyaltyRequestDTO loyaltyRequestDTO) {
        Loyalty loyalty = loyaltyRequestMapper.toEntity(loyaltyRequestDTO);

        commonValidation(loyalty);
        requiredNullOrEmpty(loyalty.getId(), "Id must be null");

        if(loyaltyRepository.findByCustomerId(loyalty.getCustomer().getId()) != null)
            throw new BadRequestException(String.format("Customer id %s already exists", loyalty.getCustomer().getId()));

        return loyaltyResponseMapper.toDTO(loyaltyRepository.save(loyalty));
    }

    @Override
    public LoyaltyResponseDTO update(LoyaltyRequestDTO loyaltyRequestDTO) {
        Loyalty loyalty = loyaltyRequestMapper.toEntity(loyaltyRequestDTO);

        commonValidation(loyalty);
        requiredValue(loyalty.getId(), "Id is required");

        return loyaltyResponseMapper.toDTO(loyaltyRepository.save(loyalty));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.loyaltyRepository.deleteById(id);
    }

    @Override
    public LoyaltyResponseDTO find(Long id) {
        return loyaltyRepository.findById(id)
                .map(loyaltyResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("Loyalty id %s not found", id)));
    }

    @Override
    public List<LoyaltyResponseDTO> findAll() {
        return this.loyaltyRepository
                .findAll()
                .stream()
                .map(loyaltyResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(Loyalty loyalty){
        requiredValue(loyalty, "Loyalty is required");
        requiredValue(loyalty.getCustomer(), "Customer is required");
        igualOrGreaterThan(loyalty.getPoints(), 0L, "Points can't be negative");
    }

    public LoyaltyResponseDTO incrementPoints(Long customerId, Long newPoints) {

        Loyalty loyalty = loyaltyRepository.findByCustomerId(customerId);

        if(loyalty == null){
            Customer customer = customerResponseMapper.toEntity(customerService.find(customerId));

            loyalty = new Loyalty(); //FIXME: try to use DTO
            loyalty.setCustomer(customer);
            loyalty.setPoints(newPoints);

            return create(loyaltyRequestMapper.toDTO(loyalty));
        }else {
            loyalty.addPoints(newPoints);

            return update(loyaltyRequestMapper.toDTO(loyalty));
        }
    }

    public void paymentWithPoints(Long customerId, BookType bookType) {
        Loyalty loyalty = loyaltyRepository.findByCustomerId(customerId);

        requiredValue(loyalty, "Customer id %s doesn't have any point");
        requiredTrue(bookType.isPaybleWithPoints(), String.format("BookType %s is not payble with points", bookType.getName()));
        igualOrGreaterThan(loyalty.getPoints(), BOOK_PRICE_WITH_POINTS,
                String.format("Customer id %s doesn't have %s points to buy with points: %s", customerId, BOOK_PRICE_WITH_POINTS, loyalty.getPoints()));

        loyalty.setPoints(loyalty.getPoints() - BOOK_PRICE_WITH_POINTS);
        update(loyaltyRequestMapper.toDTO(loyalty));
    }
}
