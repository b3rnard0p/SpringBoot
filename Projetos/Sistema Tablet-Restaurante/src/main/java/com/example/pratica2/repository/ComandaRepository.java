package com.example.pratica2.repository;

import com.example.pratica2.models.Comanda;
import com.example.pratica2.models.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    Optional<Comanda> findByMesaAndStatus(Tipo mesa, String status);
}
