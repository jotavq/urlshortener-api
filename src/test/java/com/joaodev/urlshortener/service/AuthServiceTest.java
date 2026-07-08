package com.joaodev.urlshortener.service;

import com.joaodev.urlshortener.dto.AuthResponse;
import com.joaodev.urlshortener.dto.LoginRequest;
import com.joaodev.urlshortener.dto.RegisterRequest;
import com.joaodev.urlshortener.entity.User;
import com.joaodev.urlshortener.repository.UserRepository;
import com.joaodev.urlshortener.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User userFake;

    @BeforeEach
    void setUp() {
        userFake = new User("Joao", "joao@email.com", "HashedPass");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("HashedPass");
        when(userRepository.save(any(User.class))).thenReturn(userFake);
        when(jwtService.gerarToken(anyString())).thenReturn("fake-jwt-token");

        RegisterRequest request = new RegisterRequest("Joao", "joao@email.com", "password123");

        // Act
        AuthResponse response = authService.registrar(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        assertEquals("joao@email.com", response.email());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(userFake));

        RegisterRequest request = new RegisterRequest("Joao", "joao@email.com", "password123");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.registrar(request);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        // Arrange
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(userFake));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.gerarToken(anyString())).thenReturn("fake-jwt-token");

        LoginRequest request = new LoginRequest("joao@email.com", "password123");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        // Arrange
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(userFake));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginRequest request = new LoginRequest("joao@email.com", "passError");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.login(request);
        });

    }
}