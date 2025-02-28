package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController implements BaseController<Book> {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @Override
    public Book create(Book book) {
        return this.bookService.create(book) ;
    }

    @Override
    public Book update(Book book) {
        return this.bookService.update(book);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.bookService.delete(id);

        return new ResponseEntity<>(String.format("Book id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public Book find(Long id) {
        return this.bookService.find(id);
    }

    @Override
    public List<Book> findAll() {
        return this.bookService.findAll();
    }

    @GetMapping("available")
    public List<Book> available(){
        return this.bookService.available();
    }
}
