package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
}