package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private CarrinhoDeCompra carrinhoDeCompra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public CarrinhoDeCompra getCarrinhoDeCompra() {
        return carrinhoDeCompra;
    }

    public void setCarrinhoDeCompra(CarrinhoDeCompra carrinhoDeCompra) {
        this.carrinhoDeCompra = carrinhoDeCompra;
    }
}