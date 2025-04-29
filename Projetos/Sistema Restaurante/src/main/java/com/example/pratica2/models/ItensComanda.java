package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItensComanda {

    @EmbeddedId
    private ItensComandaId id;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @MapsId("produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "comanda_id", nullable = false)
    @MapsId("comanda_id")
    private Comanda comanda;
}
