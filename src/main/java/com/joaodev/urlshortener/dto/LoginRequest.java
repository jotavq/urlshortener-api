package com.joaodev.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record LoginRequest(

        @NotBlank(message = "O email é obrigatorio")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha é obrigatoria")
        String senha
) {
}
