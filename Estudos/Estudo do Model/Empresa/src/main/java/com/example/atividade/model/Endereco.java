package com.example.atividade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class Endereco implements Serializable {

    @Getter
    @Column(name = "Rua")
    private String rua;

    @Getter
    @Column(name = "Bairro")
    private String bairro;

    @Getter
    @Column(name = "Numero")
    private int numero;

    @Getter
    @Column(name = "Complemento")
    private String complemento;
}
