package com.bookstore.mapper;

import com.bookstore.dto.UserRequestDTO;
import com.bookstore.dto.UserResponseDTO;
import com.bookstore.entity.User;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper extends BaseMapper<User, UserRequestDTO> {

}
