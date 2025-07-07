package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venda v")
    BigDecimal sumTotal();
}
