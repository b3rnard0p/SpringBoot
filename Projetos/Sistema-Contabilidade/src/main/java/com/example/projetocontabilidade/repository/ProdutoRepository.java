package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
