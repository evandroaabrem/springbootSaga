package com.example.sbkafka.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO de request para criação de usuário.
 */
public record CreateUserRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "email é obrigatório") @Email(message = "email inválido") String email
) {
}
