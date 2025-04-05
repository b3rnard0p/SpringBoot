package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "Dlocal", nullable = false)
    private String local;

    @Getter
    @ManyToOne
    @JoinColumn(name = "Dnumero", nullable = false)
    private Departamento departamento;

    // Getters e Setter
}