package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItensPedido {

    @EmbeddedId
    private ItensPedidoId id;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @MapsId("produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @MapsId("pedido_id")
    private Pedido pedido;
}
