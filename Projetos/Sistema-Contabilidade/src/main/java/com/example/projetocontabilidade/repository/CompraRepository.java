package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM Compra c")
    BigDecimal sumValorTotal();
}
