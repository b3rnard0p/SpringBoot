package com.example.pratica2.repository;

import com.example.pratica2.models.CarrinhoDeCompra;
import com.example.pratica2.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrinhoDeCompraRepository extends JpaRepository<CarrinhoDeCompra, Long> {
    Optional<CarrinhoDeCompra> findByUsuario(Usuario usuario);
}