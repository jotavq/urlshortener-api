package com.joaodev.urlshortener.service;

import com.joaodev.urlshortener.dto.CriarUrlRequest;
import com.joaodev.urlshortener.dto.UrlResponse;
import com.joaodev.urlshortener.entity.Url;
import com.joaodev.urlshortener.exception.UrlNotFoundException;
import com.joaodev.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    private Url urlFake;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080");
        urlFake = new Url("abc123", "https://google.com", "http://localhost:8080/urls/r/abc123");
    }

    @Test
    void deveCriarUrlComSucesso() {
        when(urlRepository.save(any(Url.class))).thenReturn(urlFake);

        CriarUrlRequest request = new CriarUrlRequest("https//google.com");
        UrlResponse response = urlService.criarUrl(request);

        assertNotNull(response);
        assertEquals("https://google.com", response.urlOriginal());
        assertEquals("http://localhost:8080/urls/r/abc123", response.urlCurta());
        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    void deveBuscarUrlPorCodigoComSucesso() {
        when(urlRepository.findByCodigo("abc123")).thenReturn(Optional.of(urlFake));

        UrlResponse response = urlService.buscarPorCodigo("abc123");

        assertNotNull(response);
        assertEquals("abc123", response.codigo());
    }

    @Test
    void deveLancarExcecaoQuandoCodigoNaoExiste() {
        when(urlRepository.findByCodigo("invalido")).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> {
            urlService.buscarPorCodigo("invalido");
        });
    }

    @Test
    void deveListarTodasAsUrls() {
        when(urlRepository.findAll()).thenReturn(List.of(urlFake));

        List<UrlResponse> lista = urlService.listarTodas();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void deveIncrementarAcessoAoRedirecionar() {
        when(urlRepository.findByCodigo("abc123")).thenReturn(Optional.of(urlFake));

        String urlOriginal = urlService.redirecionar("abc123");

        assertEquals("https://google.com", urlOriginal);
        assertEquals(1L, urlFake.getAcessos());
        verify(urlRepository, times(1)).save(any(Url.class));

    }

}
