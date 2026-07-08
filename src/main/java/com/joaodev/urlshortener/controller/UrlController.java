package com.joaodev.urlshortener.controller;

import com.joaodev.urlshortener.dto.CriarUrlRequest;
import com.joaodev.urlshortener.dto.UrlResponse;
import com.joaodev.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/urls")
@Tag(name = "URL Controller", description = "Endpoints para gerenciamento de URLs")
@SecurityRequirement(name = "Bearer Authentication")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Criar uma nova URL encurtada", description = "Recebe uma URL original e retorna uma versão encurtada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "URL criada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @PostMapping
    public ResponseEntity<UrlResponse>  criar(@RequestBody CriarUrlRequest request) {
        UrlResponse response = urlService.criarUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas as URLs")
    @GetMapping
    public ResponseEntity<List<UrlResponse>> listar() {
        List<UrlResponse> urls = urlService.listarTodas();
        return ResponseEntity.ok(urls);
    }

    @Operation(summary = "Buscar URL por código")
    @GetMapping("/{codigo}")
    public ResponseEntity<UrlResponse> buscar(
            @Parameter(description = "Código da URL curta", example = "abc123")
            @PathVariable String codigo) {
        UrlResponse response = urlService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redirecionar", description = "Redireciona para URL Original e incrementa o contador de acessos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirecionamento bem-sucedido"),
            @ApiResponse(responseCode = "404", description = "URL não encontrada")
    })
    @GetMapping("/r/{codigo}")
    public ResponseEntity<Void> redirecionar(@PathVariable String codigo) {
        String urlOriginal = urlService.redirecionar(codigo);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", urlOriginal)
                .build();
    }
}
