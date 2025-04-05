package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Trabalha_em")
public class TrabalhaEm {

    @Getter
    @EmbeddedId
    private TrabalhaEmId id;

    @Getter
    @Column(name = "Horas")
    private int horas;

    @ManyToOne
    @MapsId("funcionarioId")
    @JoinColumn(name = "Fcpf", nullable = false)
    private Funcionario funcionario;

    @ManyToOne
    @MapsId("projetoId")
    @JoinColumn(name = "Pnr", nullable = false)
    private Projeto projeto;
}
