package com.joaodev.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record RegisterRequest(

        @NotBlank(message = "O nome é obrigatorio")
        String nome,

        @NotBlank(message = "O Email é obrigatorio")
        @Email(message = "Email Inválido")
        String email,

        @NotBlank(message = "A senha é obrigatoria")
        @Size(min = 8, message = "A senha deve ter no minimo 8 caracteres")
        String senha
) {
}
