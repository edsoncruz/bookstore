package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.BookTypeRequestDTO;
import com.bookstore.dto.BookTypeResponseDTO;
import com.bookstore.service.BookTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookTypes")
public class BookTypeController implements BaseController<BookTypeRequestDTO, BookTypeResponseDTO> {

    private final BookTypeService bookTypeService;

    public BookTypeController(BookTypeService bookTypeService){
        this.bookTypeService = bookTypeService;
    }

    @Override
    public BookTypeResponseDTO create(BookTypeRequestDTO bookTypeRequestDTO) {
        return this.bookTypeService.create(bookTypeRequestDTO);
    }

    @Override
    public BookTypeResponseDTO update(BookTypeRequestDTO bookTypeRequestDTO) {
        return this.bookTypeService.update(bookTypeRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.bookTypeService.delete(id);

        return new ResponseEntity<>(String.format("BookType id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public BookTypeResponseDTO find(Long id) {
        return this.bookTypeService.find(id);
    }

    @Override
    public List<BookTypeResponseDTO> findAll() {
        return this.bookTypeService.findAll();
    }
}
