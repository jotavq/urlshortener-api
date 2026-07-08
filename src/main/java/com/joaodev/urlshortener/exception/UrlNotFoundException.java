package com.joaodev.urlshortener.exception;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String codigo) {
        super("URL não encontrada para o codigo: " + codigo);
    }
}
