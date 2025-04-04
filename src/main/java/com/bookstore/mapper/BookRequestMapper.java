package com.bookstore.mapper;

import com.bookstore.dto.BookRequestDTO;
import com.bookstore.dto.BookResponseDTO;
import com.bookstore.entity.Book;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookRequestMapper extends BaseMapper<Book, BookRequestDTO> {

}
