package com.example.pratica2.models;

public enum Tipo {
    ADMIN("Admin"),
    MESA1("Mesa 1"),
    MESA2("Mesa 2"),
    MESA3("Mesa 3");

    private final String tipo;

    Tipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
