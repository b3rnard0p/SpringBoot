package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Patrimonio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrimonioRepository extends JpaRepository<Patrimonio, Long> {
}