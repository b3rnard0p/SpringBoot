package com.example.projetocontabilidade.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Bem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal preco;

    private int quantidade;

    @OneToMany(mappedBy = "bem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraBens> comprasBens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<CompraBens> getComprasBens() {
        return comprasBens;
    }

    public void setComprasBens(List<CompraBens> comprasBens) {
        this.comprasBens = comprasBens;
    }
}
