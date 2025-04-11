package com.aula203.atividade1.repository;

import com.aula203.atividade1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Encontrar User por Nome
    List<User> findByNome(String nome);

    // Encontrar User por Nome e Email
    List<User> findByNomeAndEmail(String nome, String email);

    // Encontrar User por Email
    User findByEmail(String email);

    // Verificar se Email existe
    boolean existsByEmail(String email);

    // Encontrar Users por parte do nome
    List<User> findByNomeContaining(String parteNome);

    // Que tem e-mail "Gmail.com"
    List<User> findByEmailContaining(String dominio);

    // Que começam com "Go"
    List<User> findByNomeStartingWith(String prefixo);

    // Recuperar todas as Users ordenadas por nome
    List<User> findAllByOrderByNomeAsc();

    // As últimas 3 Users de uma lista ordenada por nome
    @Query("SELECT u FROM User u ORDER BY u.nome DESC LIMIT 3")
    List<User> findTop3ByOrderByNomeDesc();

    // Delete um User por Email
    void deleteByEmail(String email);

    // Liste os users que tem somente 1 phone
    @Query("SELECT u FROM User u WHERE SIZE(u.phones) = 1")
    List<User> findUsersWithSinglePhone();
}
