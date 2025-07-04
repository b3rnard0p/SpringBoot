package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByCapitalSocial(BigDecimal capitalSocial);
}
