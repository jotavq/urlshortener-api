package com.joaodev.urlshortener.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUrlNotFound(
            UrlNotFoundException ex) {

        Map<String, Object> erro = Map.of(
                "status", 404,
                "erro", "URL não encontrada",
                "mensagem", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleErroGenerico(Exception ex) {
        // Log full stacktrace for troubleshooting
        log.error("Unhandled exception caught by GlobalExceptionHandler", ex);

        Map<String, Object> erro = Map.of(
                "status", 500,
                "erro", "Erro interno",
                "mensagem", "Ocorreu um erro inesperado",
                "timestamp", LocalDateTime.now().toString()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);

    }
}
