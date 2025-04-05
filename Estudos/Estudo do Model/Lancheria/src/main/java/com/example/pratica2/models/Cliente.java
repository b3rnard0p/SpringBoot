package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String telefone;

    private String enderecoEntrega;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> HistoricoPedido;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private CarrinhoDeCompra carrinhoDeCompra;
}