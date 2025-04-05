package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime Data;

    private double ValorTotal;

    private String status;

    private String instrucoesEntrega;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItensPedido> ItensPedido;

    @OneToOne(mappedBy = "pedido")
    private Pagamento pagamento;
}