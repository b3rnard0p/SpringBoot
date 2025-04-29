package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime Data;

    private double ValorTotal;

    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo mesa;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL)
    private List<ItensComanda> ItensComanda;
}