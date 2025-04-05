package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class CarrinhoDeCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "carrinhoDeCompra", cascade = CascadeType.ALL)
    private List<ItensCarrinho> ItensCarrinho;
}