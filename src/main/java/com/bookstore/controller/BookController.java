package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.BookRequestDTO;
import com.bookstore.dto.BookResponseDTO;
import com.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController implements BaseController<BookRequestDTO, BookResponseDTO> {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @Override
    public BookResponseDTO create(BookRequestDTO bookRequestDTO){
        return this.bookService.create(bookRequestDTO);
    }

    @Override
    public BookResponseDTO update(BookRequestDTO bookRequestDTO) {
        return this.bookService.update(bookRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.bookService.delete(id);

        return new ResponseEntity<>(String.format("Book id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public BookResponseDTO find(Long id) {
        return this.bookService.find(id);
    }

    @Override
    public List<BookResponseDTO> findAll() {
        return this.bookService.findAll();
    }

    @GetMapping("available")
    public List<BookResponseDTO> available(){
        return this.bookService.available();
    }
}
