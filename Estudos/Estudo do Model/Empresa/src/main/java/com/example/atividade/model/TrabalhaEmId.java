package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class TrabalhaEmId implements Serializable {

    @Getter
    @Column(name = "Fcpf")
    private String funcionarioId;

    @Getter
    @Column(name = "Pnr")
    private Long projetoId;

    public TrabalhaEmId(String funcionarioId, Long projetoId) {
        this.funcionarioId = funcionarioId;
        this.projetoId = projetoId;
    }

    public TrabalhaEmId() {

    }
}