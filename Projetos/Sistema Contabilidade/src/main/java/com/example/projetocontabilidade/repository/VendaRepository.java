package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
