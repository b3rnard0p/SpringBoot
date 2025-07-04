package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Bem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BemRepository extends JpaRepository<Bem, Long> {
}
