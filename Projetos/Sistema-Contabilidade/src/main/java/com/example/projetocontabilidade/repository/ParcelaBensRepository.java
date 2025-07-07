package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.ParcelaBens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ParcelaBensRepository extends JpaRepository<ParcelaBens, Long> {
    List<ParcelaBens> findByPagaFalseOrderByDataVencimento();
    List<ParcelaBens> findByCompraBensIdAndPagaFalseOrderByNumeroAsc(Long compraBensId);
    @Query("SELECT SUM(p.valor) FROM ParcelaBens p WHERE p.paga = false")
    BigDecimal sumValorByPagaFalse();
}
