package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
}
