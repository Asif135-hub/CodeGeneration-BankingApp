package nl.inholland.codegen.bankingapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtmLoginRequestDto(
    @NotBlank @Email String email,
    @NotBlank String password
) {}
