package com.example.pratica2.models;

public enum Status {
    DISPONIVEL("Disponível"),
    INDISPONIVEL("Indisponível");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
