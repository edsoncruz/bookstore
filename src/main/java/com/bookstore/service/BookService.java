package com.bookstore.service;

import com.bookstore.dto.BookRequestDTO;
import com.bookstore.dto.BookResponseDTO;
import com.bookstore.dto.BookTypeResponseDTO;
import com.bookstore.entity.Book;
import com.bookstore.exception.NotFoundException;
import com.bookstore.mapper.BookRequestMapper;
import com.bookstore.mapper.BookResponseMapper;
import com.bookstore.mapper.BookTypeRequestMapper;
import com.bookstore.mapper.BookTypeResponseMapper;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class BookService implements BaseService<BookRequestDTO, BookResponseDTO> {

    private final BookRepository bookRepository;
    private final BookTypeService bookTypeService;
    private final BookRequestMapper bookRequestMapper;
    private final BookResponseMapper bookResponseMapper;
    private final BookTypeResponseMapper bookTypeResponseMapper;

    public BookService(BookRepository bookRepository, BookTypeService bookTypeService, BookTypeRequestMapper bookTypeRequestMapper, BookRequestMapper bookRequestMapper, BookResponseMapper bookResponseMapper, BookTypeResponseMapper bookTypeResponseMapper){
        this.bookRepository = bookRepository;
        this.bookTypeService = bookTypeService;
        this.bookRequestMapper = bookRequestMapper;
        this.bookResponseMapper = bookResponseMapper;
        this.bookTypeResponseMapper = bookTypeResponseMapper;
    }

    @Override
    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        Book book = bookRequestMapper.toEntity(bookRequestDTO);
        BookTypeResponseDTO bookTypeResponseDTO = bookTypeService.find(book.getBookType().getId());
        book.setBookType(bookTypeResponseMapper.toEntity(bookTypeResponseDTO));

        commonValidation(book);
        requiredNullOrEmpty(book.getId(), "Id must be null"); //TODO: maybe I do a new DTO just for registration, and remove this validation
        book.setDateCreated(LocalDateTime.now());
        book = bookRepository.save(book);

        return bookResponseMapper.toDTO(book);
    }

    @Override
    public BookResponseDTO update(BookRequestDTO bookRequestDTO) {
        Book book = bookRequestMapper.toEntity(bookRequestDTO);

        commonValidation(book);
        requiredValue(book.getId(), "Id is required");

        return bookResponseMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.bookRepository.deleteById(id);
    }

    @Override
    public BookResponseDTO find(Long id) {
         return bookRepository.findById(id)
                 .map(bookResponseMapper::toDTO)
                 .orElseThrow(() -> new NotFoundException("Book id %s not found".formatted(id)));
    }

    @Override
    public List<BookResponseDTO> findAll() {
        return this.bookRepository.findAll()
                .stream()
                .map(bookResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> available() {
        return this.bookRepository.findByAmountGreaterThanOrderByTitle(0L)
                .stream()
                .map(bookResponseMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void commonValidation(Book book){
        requiredValue(book, "Book is required");
        //requiredValue(book.getTitle(), "Title is required"); //TODO: Testing validation
        //requiredValue(book.getPrice(), "Price is required"); //TODO: Testing validation
        requiredValue(book.getAmount(), "Amount is required");
        igualOrGreaterThan(book.getAmount(), 0L, "Amount can't be negative");
        requiredValue(book.getBookType(), "BookType is required");
    }

    public void decreaseAmount(Long bookId, Long amountToDecrease){
        Book book = bookResponseMapper.toEntity(find(bookId));

        book.setAmount(book.getAmount() - amountToDecrease);

        update(bookRequestMapper.toDTO(book));
    }
}
