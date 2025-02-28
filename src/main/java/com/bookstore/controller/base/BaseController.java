package com.bookstore.controller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BaseController<T> {
    @PostMapping
    T create(@RequestBody T entity);

    @PutMapping
    T update(@RequestBody T entity);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    T find(@PathVariable("id") Long id);

    @GetMapping
    List<T> findAll();
}
