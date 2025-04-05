package com.example.atividade.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Departamento")
public class Departamento {

    @Getter
    @Id
    @Column(name = "Dnumero")
    private Long dNumero;

    @Getter
    @Column(name = "Nome", nullable = false, unique = true)
    private String nome;

    @Getter
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "Data_inicio_gerente")
    private Date dataInicioGerente;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<Funcionario> listaDeFuncionario;

    @Getter
    @OneToOne
    @JoinColumn(name = "Gerente_id")
    private Funcionario gerente;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<Localizacao> localizacoes;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    List<Projeto> projetos;

}