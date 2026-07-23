package com.joaodev.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CriarUrlRequest(

        @NotBlank(message = "A URL original é obrigatoria")
        @URL(message = "A URL informada é inválida")
        String urlOriginal
) {
}
