package com.bookstore.dto;

import com.bookstore.dto.base.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerResponseDTO(
    @NotNull
    Long id,
    @NotBlank
    String name,
    @Email
    String email,
    @NotBlank
    UserResponseDTO user
)implements BaseDTO{}
