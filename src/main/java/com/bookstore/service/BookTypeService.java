package com.bookstore.service;

import com.bookstore.entity.BookType;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.BookTypeRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class BookTypeService implements BaseService<BookType> {

    private final BookTypeRepository bookTypeRepository;

    public BookTypeService(BookTypeRepository bookTypeRepository){
        this.bookTypeRepository = bookTypeRepository;
    }

    @Override
    public BookType create(BookType bookType) {
        commonValidation(bookType);
        requiredNullOrEmpty(bookType.getId(), "Id must be null");

        return bookTypeRepository.save(bookType);
    }

    @Override
    public BookType update(BookType bookType) {
        commonValidation(bookType);
        requiredValue(bookType.getId(), "Id is required");

        return this.bookTypeRepository.save(bookType);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.bookTypeRepository.deleteById(id);
    }

    @Override
    public BookType find(Long id) {
        Optional<BookType> bookOptional = this.bookTypeRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("BookType id %s not found", id));
        }
    }

    @Override
    public List<BookType> findAll() {
        return this.bookTypeRepository.findAll();
    }

    private void commonValidation(BookType bookType){
        requiredValue(bookType, "BookType is required");
        requiredValue(bookType.getName(), "Name is required");
        betweenValue(bookType.getDiscountOverall(), BigDecimal.valueOf(0), BigDecimal.valueOf(1), "DiscountOverall must be between 0 and 1");
        positiveValueOrNull(bookType.getMinimalBundle(), "MinimalBundle must be positive");
        betweenValue(bookType.getDiscountBundle(), BigDecimal.valueOf(0), BigDecimal.valueOf(1), "DiscountBundle must be between 0 and 1");
    }
}
