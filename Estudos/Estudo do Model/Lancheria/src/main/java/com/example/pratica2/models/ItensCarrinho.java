package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItensCarrinho {

    @EmbeddedId
    private ItensCarrinhoId id;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @MapsId("produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "Carrinho_de_compra_id", nullable = false)
    @MapsId("Carrinho_de_compra_id")
    private CarrinhoDeCompra carrinhoDeCompra;
}