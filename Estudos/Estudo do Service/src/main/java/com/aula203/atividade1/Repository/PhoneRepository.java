package com.aula203.atividade1.Repository;

import com.aula203.atividade1.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
