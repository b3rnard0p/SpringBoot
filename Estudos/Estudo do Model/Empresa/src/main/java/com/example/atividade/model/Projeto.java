package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "Projeto")
public class Projeto {

    @Getter
    @Id
    @Column(name = "Pnumero", unique = true)
    private Long numero;

    @Getter
    @Column(name = "Projnome")
    private String nome;

    @Getter
    @Column(name = "Projlocal")
    private String local;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private List<TrabalhaEm> alocacao;

    @Getter
    @ManyToOne
    @JoinColumn(name = "Dnum", nullable = false)
    private Departamento departamento;
}