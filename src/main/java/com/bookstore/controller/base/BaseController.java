package com.bookstore.controller.base;

import com.bookstore.dto.base.BaseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BaseController<Q extends BaseDTO, R extends BaseDTO> {
    @PostMapping
    R create(@Valid @RequestBody Q entity);

    @PutMapping
    R update(@RequestBody Q entity);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    R find(@PathVariable("id") Long id);

    @GetMapping
    List<R> findAll();
}
