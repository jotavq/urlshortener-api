package com.joaodev.urlshortener.controller;

import com.joaodev.urlshortener.dto.AuthResponse;
import com.joaodev.urlshortener.dto.LoginRequest;
import com.joaodev.urlshortener.dto.RegisterRequest;
import com.joaodev.urlshortener.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Criar conta", description = "Registra um novo usuário e retorna o token JWT")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @Operation(summary = "Fazer login", description = "Autentica o usuário e retorna o token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
