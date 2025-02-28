package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.entity.BookType;
import com.bookstore.service.BookTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookTypes")
public class BookTypeController implements BaseController<BookType> {

    private final BookTypeService bookTypeService;

    public BookTypeController(BookTypeService bookTypeService){
        this.bookTypeService = bookTypeService;
    }

    @Override
    public BookType create(BookType bookType) {
        return this.bookTypeService.create(bookType) ;
    }

    @Override
    public BookType update(BookType bookType) {
        return this.bookTypeService.update(bookType);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.bookTypeService.delete(id);

        return new ResponseEntity<>(String.format("BookType id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public BookType find(Long id) {
        return this.bookTypeService.find(id);
    }

    @Override
    public List<BookType> findAll() {
        return this.bookTypeService.findAll();
    }
}
