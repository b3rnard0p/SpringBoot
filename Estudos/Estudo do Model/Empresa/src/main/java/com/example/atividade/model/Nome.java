package com.example.atividade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class Nome implements Serializable {

    @Getter
    @Column(name = "Pnome")
    private String pNome;

    @Getter
    @Column(name = "Mnome")
    private String mInicial;

    @Getter
    @Column(name = "Unome")
    private String uNome;
}