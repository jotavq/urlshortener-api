package com.joaodev.urlshortener.service;

import com.joaodev.urlshortener.dto.AuthResponse;
import com.joaodev.urlshortener.dto.LoginRequest;
import com.joaodev.urlshortener.dto.RegisterRequest;
import com.joaodev.urlshortener.entity.User;
import com.joaodev.urlshortener.repository.UserRepository;
import com.joaodev.urlshortener.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse registrar(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User(
                request.nome(),
                request.email(),
                passwordEncoder.encode(request.senha())
        );

        userRepository.save(user);
        String token = jwtService.gerarToken(user.getEmail());

        return new AuthResponse(token, user.getNome(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), user.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }
        String token = jwtService.gerarToken(user.getEmail());
        return new AuthResponse(token, user.getNome(), user.getEmail());
    }
}
