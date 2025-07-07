package com.example.projetocontabilidade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projetocontabilidade.model.Despesas;

public interface DespesasRepository extends JpaRepository<Despesas, Long> {
    java.util.List<Despesas> findByPagoFalse();
}
