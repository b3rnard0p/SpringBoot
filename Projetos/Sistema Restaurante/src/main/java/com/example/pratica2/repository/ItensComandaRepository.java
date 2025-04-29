package com.example.pratica2.repository;

import com.example.pratica2.models.ItensComanda;
import com.example.pratica2.models.ItensComandaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItensComandaRepository extends JpaRepository<ItensComanda, ItensComandaId> {

    List<ItensComanda> findByComandaId(Long comandaId);
}
