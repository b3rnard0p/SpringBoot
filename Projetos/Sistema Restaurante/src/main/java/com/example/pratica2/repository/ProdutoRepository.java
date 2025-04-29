package com.example.pratica2.repository;

import com.example.pratica2.models.Categoria;
import com.example.pratica2.models.Produto;
import com.example.pratica2.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByStatus(Status status);
}
