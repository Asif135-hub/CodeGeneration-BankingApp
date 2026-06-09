package nl.inholland.codegen.bankingapp.dtos;

public record AtmLoginResponseDTO(
    String firstName,
    String lastName,
    String email,
    String bsn,
    String phoneNumber
) {}
