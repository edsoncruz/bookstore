package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookType;
import com.bookstore.exception.NotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bookstore.helper.ValidationHelper.*;

@Service
public class BookService implements BaseService<Book> {

    private final BookRepository bookRepository;
    private final BookTypeService bookTypeService;

    public BookService(BookRepository bookRepository, BookTypeService bookTypeService){
        this.bookRepository = bookRepository;
        this.bookTypeService = bookTypeService;
    }

    @Override
    public Book create(Book book) {
        commonValidation(book);
        requiredNullOrEmpty(book.getId(), "Id must be null");
        book.setDateCreated(LocalDateTime.now());

        BookType bookType = bookTypeService.find(book.getBookType().getId());
        book.setBookType(bookType);

        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        commonValidation(book);
        requiredValue(book.getId(), "Id is required");

        return this.bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        find(id);

        this.bookRepository.deleteById(id);
    }

    @Override
    public Book find(Long id) {
        Optional<Book> bookOptional = this.bookRepository.findById(id);

        if(bookOptional.isPresent()) {
            return bookOptional.get();
        }else {
            throw new NotFoundException(String.format("Book id %s not found", id));
        }
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    public List<Book> available() {
        return this.bookRepository.findByAmountGreaterThanOrderByTitle(0L);
    }

    private void commonValidation(Book book){
        requiredValue(book, "Book is required");
        requiredValue(book.getTitle(), "Title is required");
        requiredValue(book.getPrice(), "Price is required");
        requiredValue(book.getAmount(), "Amount is required");
        igualOrGreaterThan(book.getAmount(), 0L, "Amount can't be negative");
        requiredValue(book.getBookType(), "BookType is required");
    }

    public void decreaseAmount(Long bookId, Long amountToDecrease){
        Book book = find(bookId);

        book.setAmount(book.getAmount() - amountToDecrease);

        update(book);
    }
}
