package com.bookstore.mapper;

import com.bookstore.dto.UserResponseDTO;
import com.bookstore.entity.User;
import com.bookstore.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper extends BaseMapper<User, UserResponseDTO> {

}
