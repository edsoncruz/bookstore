package com.bookstore.service.base;

import java.util.List;

public interface BaseService<T>{
    T create(T entity);

    T update(T entity);

    void delete(Long id);

    T find(Long id);

    List<T> findAll();
}
