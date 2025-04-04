package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import com.bookstore.enums.Role;

public record UserResponseDTO(
        Long id,
        String username,
        String password,
        Role role
) implements BaseDTO {}
