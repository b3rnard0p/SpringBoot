package com.example.pratica2.repository;

import com.example.pratica2.models.ItensCarrinho;
import com.example.pratica2.models.ItensCarrinhoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensCarrinhoRepository extends JpaRepository<ItensCarrinho, ItensCarrinhoId> {
}
