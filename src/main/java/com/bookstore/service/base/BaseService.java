package com.bookstore.service.base;

import com.bookstore.dto.base.BaseDTO;

import java.util.List;

public interface BaseService<Q extends BaseDTO, R extends BaseDTO>{
    R create(Q dtoRequest);

    R update(Q dtoRequest);

    void delete(Long id);

    R find(Long id);

    List<R> findAll();
}
