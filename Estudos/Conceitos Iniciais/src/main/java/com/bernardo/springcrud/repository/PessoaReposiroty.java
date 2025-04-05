package com.bernardo.springcrud.repository;

import com.bernardo.springcrud.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaReposiroty extends JpaRepository<Pessoa, Integer> {
    Optional<Pessoa> findByCpf(String cpf);
}
