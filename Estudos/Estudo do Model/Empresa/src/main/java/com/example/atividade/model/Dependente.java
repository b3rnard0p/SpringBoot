package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Dependente")
public class Dependente {

    @Getter
    @EmbeddedId
    private DependenteId dependenteId;

    @Getter
    @Column(name ="Sexo", length = (1))
    private char sexo;

    @Getter
    @Temporal(TemporalType.DATE)
    @Column(name = "DataNasc")
    private Date dataNasc;

    @Getter
    @Column(name = "Parentesco")
    private String parentesco;

    @ManyToOne
    @JoinColumn(name = "Fcpf", nullable = false)
    @MapsId("funcionarioId")
    private Funcionario funcionario;
}