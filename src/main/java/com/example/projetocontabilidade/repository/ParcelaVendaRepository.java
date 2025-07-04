package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.ParcelaVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ParcelaVendaRepository extends JpaRepository<ParcelaVenda, Long> {
    List<ParcelaVenda> findByPagaFalseOrderByDataVencimento();
    List<ParcelaVenda> findByVendaIdAndPagaFalseOrderByNumeroAsc(Long vendaId);
    @Query("SELECT SUM(p.valor) FROM ParcelaVenda p WHERE p.paga = false")
    BigDecimal sumValorByPagaFalse();

}
