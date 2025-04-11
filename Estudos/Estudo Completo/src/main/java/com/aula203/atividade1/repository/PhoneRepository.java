package com.aula203.atividade1.repository;

import com.aula203.atividade1.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    // Encontrar telefones por user name
    @Query("SELECT p FROM Phone p JOIN p.user u WHERE u.nome = :nome")
    List<Phone> findByUserName(@Param("nome") String nome);

    // Exemplo com @query personalizada
    @Query("SELECT p FROM Phone p WHERE p.user.id = :userId AND p.number LIKE :prefixo%")
    List<Phone> findByUserAndNumberStartingWith(@Param("userId") Long userId,
                                                @Param("prefixo") String prefixo);
}