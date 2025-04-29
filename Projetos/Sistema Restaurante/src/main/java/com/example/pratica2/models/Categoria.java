package com.example.pratica2.models;

public enum Categoria {
    HAMBURGUER("Hambúrguer"),
    SOBREMESA("Sobremesa"),
    BEBIDA("Bebida");

    private final String nome;

    Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}