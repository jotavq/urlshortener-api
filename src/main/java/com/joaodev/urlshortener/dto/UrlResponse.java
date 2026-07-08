package com.joaodev.urlshortener.dto;

import java.time.LocalDateTime;

public record UrlResponse(
        String codigo,
        String urlCurta,
        String urlOriginal,
        Long acessos,
        LocalDateTime criadaEm
) {}
