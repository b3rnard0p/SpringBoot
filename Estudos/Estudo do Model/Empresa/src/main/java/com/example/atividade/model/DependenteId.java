package com.example.atividade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class DependenteId implements Serializable {

    @Getter
    @Column(name = "Fcpf")
    private String funcionarioId;

    @Getter
    @Column (name = "Nome_dependente")
    private String nome;
}
