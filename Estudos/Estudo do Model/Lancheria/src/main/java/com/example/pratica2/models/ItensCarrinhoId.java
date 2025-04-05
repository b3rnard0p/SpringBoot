package com.example.pratica2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ItensCarrinhoId implements Serializable {

    @Column
    private Long produto_id;

    @Column
    private Long Carrinho_de_compra_id;
}
