package com.bookstore.service;

import com.bookstore.dto.BookTypeRequestDTO;
import com.bookstore.dto.BookTypeResponseDTO;
import com.bookstore.entity.BookType;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.BookTypeRequestMapper;
import com.bookstore.mapper.BookTypeResponseMapper;
import com.bookstore.repository.BookTypeRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class BookTypeService implements BaseService<BookTypeRequestDTO, BookTypeResponseDTO> {

    private final BookTypeRepository bookTypeRepository;
    private final BookTypeResponseMapper bookTypeResponseMapper;
    private final BookTypeRequestMapper bookTypeRequestMapper;

    public BookTypeService(BookTypeRepository bookTypeRepository, BookTypeResponseMapper bookTypeResponseMapper, BookTypeRequestMapper bookTypeRequestMapper){
        this.bookTypeRepository = bookTypeRepository;
        this.bookTypeResponseMapper = bookTypeResponseMapper;
        this.bookTypeRequestMapper = bookTypeRequestMapper;
    }

    @Override
    public BookTypeResponseDTO create(BookTypeRequestDTO bookTypeRequestDTO) {
        BookType bookType = bookTypeRequestMapper.toEntity(bookTypeRequestDTO);

        commonValidation(bookType);
        requiredNullOrEmpty(bookType.getId(), "Id must be null");

        return bookTypeResponseMapper.toDTO(bookTypeRepository.save(bookType));
    }

    @Override
    public BookTypeResponseDTO update(BookTypeRequestDTO bookTypeRequestDTO) {
        BookType bookType = bookTypeRequestMapper.toEntity(bookTypeRequestDTO);
        commonValidation(bookType);
        requiredValue(bookType.getId(), "Id is required");

        return bookTypeResponseMapper.toDTO(bookTypeRepository.save(bookType));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.bookTypeRepository.deleteById(id);
    }

    @Override
    public BookTypeResponseDTO find(Long id) {
        return this.bookTypeRepository
                .findById(id)
                .map(bookTypeResponseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(String.format("BookType id %s not found", id)));
    }

    @Override
    public List<BookTypeResponseDTO> findAll() {
        return this.bookTypeRepository.findAll()
                .stream()
                .map(bookTypeResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(BookType bookType){
        requiredValue(bookType, "BookType is required");
        requiredValue(bookType.getName(), "Name is required");
        betweenValue(bookType.getDiscountOverall(), BigDecimal.valueOf(0), BigDecimal.valueOf(1), "DiscountOverall must be between 0 and 1");
        positiveValueOrNull(bookType.getMinimalBundle(), "MinimalBundle must be positive");
        betweenValue(bookType.getDiscountBundle(), BigDecimal.valueOf(0), BigDecimal.valueOf(1), "DiscountBundle must be between 0 and 1");
    }
}
