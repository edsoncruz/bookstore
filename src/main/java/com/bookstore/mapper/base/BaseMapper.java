package com.bookstore.mapper.base;

import com.bookstore.dto.base.BaseDTO;
import com.bookstore.entity.base.BaseEntity;

import java.util.List;


public interface BaseMapper<E extends BaseEntity, D extends BaseDTO> {

    E toEntity(D dto);

    D toDTO(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDTO(List<E> entityList);
}
