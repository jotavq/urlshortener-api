package com.joaodev.urlshortener.service;

import com.joaodev.urlshortener.dto.CriarUrlRequest;
import com.joaodev.urlshortener.dto.UrlResponse;
import com.joaodev.urlshortener.entity.Url;
import com.joaodev.urlshortener.exception.UrlNotFoundException;
import com.joaodev.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlResponse criarUrl(CriarUrlRequest request) {
        String codigo = gerarCodigo();
        String urlCurta = "http://localhost:8080/" + codigo;

        Url url = new Url(codigo, request.urlOriginal(), urlCurta);
        Url salva = urlRepository.save(url);

        return converterParaResponse(salva);
    }

    public UrlResponse buscarPorCodigo(String codigo) {
        Url url = urlRepository.findByCodigo(codigo)
                .orElseThrow(() -> new UrlNotFoundException(codigo));

        return converterParaResponse(url);
    }

    public String redirecionar(String codigo) {
        Url url = urlRepository.findByCodigo(codigo)
                .orElseThrow(() -> new UrlNotFoundException(codigo));

        url.incrementarAcesso();
        urlRepository.save(url);

        return url.getUrlOriginal();
    }

    public List<UrlResponse> listarTodas() {
        return urlRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    // Métodos privados - detalhes de implentação

    private String gerarCodigo() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private UrlResponse converterParaResponse(Url url) {
        return new UrlResponse(
                url.getCodigo(),
                url.getUrlCurta(),
                url.getUrlOriginal(),
                url.getAcessos(),
                url.getCriadaEm()
        );
    }
}
