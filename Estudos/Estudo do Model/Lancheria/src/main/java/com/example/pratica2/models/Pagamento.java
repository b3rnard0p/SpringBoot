package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String MetodoPagamento;

    private double Valor;

    private String status;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}