package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.CompraBens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CompraBensRepository extends JpaRepository<CompraBens, Long> {
    @Query("SELECT COALESCE(SUM(c.valorTotal), 0) FROM CompraBens c")
    BigDecimal sumValorTotal();
}
