package com.example.projetocontabilidade.repository;

import com.example.projetocontabilidade.model.ICMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICMSRepository extends JpaRepository<ICMS, Long> {
}
