package com.bookstore.mapper;

import com.bookstore.dto.BookTypeResponseDTO;
import com.bookstore.entity.BookType;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookTypeResponseMapper extends BaseMapper<BookType, BookTypeResponseDTO> {

}
