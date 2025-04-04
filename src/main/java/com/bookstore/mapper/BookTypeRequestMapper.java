package com.bookstore.mapper;

import com.bookstore.dto.BookTypeRequestDTO;
import com.bookstore.entity.BookType;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookTypeRequestMapper extends BaseMapper<BookType, BookTypeRequestDTO> {

}
