package com.joaodev.urlshortener.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name= "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "url_original", nullable = false)
    private String urlOriginal;

    @Column(name = "url_curta", nullable = false)
    private String urlCurta;

    @Column(nullable = false)
    private  long acessos;

    @Column(nullable = false)
    private boolean ativa;

    @Column(name = "criada_em")
    private LocalDateTime criadaEm;

    public Url() {}

    public Url(String codigo, String urlOriginal, String urlCurta) {
        this.codigo = codigo;
        this.urlOriginal = urlOriginal;
        this.urlCurta = urlCurta;
        this.acessos = 0L;
        this.ativa = true;
        this.criadaEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getUrlOriginal() { return urlOriginal; }
    public String getUrlCurta() { return urlCurta; }
    public Long getAcessos() { return acessos; }
    public boolean isAtiva() { return ativa; }
    public LocalDateTime getCriadaEm() { return criadaEm; }

    public void incrementarAcesso() {
        this.acessos++;
    }
    public void desativar() {
        this.ativa = false;
    }
}
