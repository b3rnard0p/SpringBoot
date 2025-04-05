package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private double preco;

    private String categoria;

    private boolean status;

    private String imagemUrl;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ItensPedido> ItensPedido;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ItensCarrinho> ItensCarrinho;
}
