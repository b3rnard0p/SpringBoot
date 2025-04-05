package com.example.atividade.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Funcionario")
public class Funcionario {

    @Getter
    @Id
    @Column(name="Cpf", length = 11)
    private String cpf;

    @Getter
    @Embedded
    private Nome nome;

    @Getter
    @Temporal(TemporalType.DATE)
    @Column(name = "DataNasc")
    private Date dataNasc;

    @Getter
    @Column(name = "Salario")
    private BigDecimal salario;

    @Getter
    @Column(name = "Sexo")
    private char sexo;

    @Getter
    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<Dependente> dependentes;

    @ManyToOne
    @JoinColumn(name = "Dnr", nullable = false)
    private Departamento departamento;

    @OneToOne(mappedBy = "gerente")
    private Departamento departamentoGerenciado;

    @ManyToOne
    @JoinColumn(name = "Cpf_Supervisor")
    private Funcionario supervisor;

    @OneToMany(mappedBy = "supervisor")
    private List<Funcionario> supervisionados;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<TrabalhaEm> alocacao;
}