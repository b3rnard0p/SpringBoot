package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.ParcelaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ParcelaCompraRepository extends JpaRepository<ParcelaCompra, Long> {
    List<ParcelaCompra> findByPagaFalseOrderByDataVencimento();
    List<ParcelaCompra> findByCompraIdAndPagaFalseOrderByNumeroAsc(Long compraId);
    @Query("SELECT SUM(p.valor) FROM ParcelaCompra p WHERE p.paga = false")
    BigDecimal sumValorByPagaFalse();
}
