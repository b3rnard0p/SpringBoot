package com.example.pratica2.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Nome;

    private String senha;

    private String cargo;

    private String detalhesDeContato;
}