package com.aula203.atividade1.records;

import com.aula203.atividade1.model.Phone;

import java.util.List;

public record GetUserDTO(Long id, String nome, String email, List<Phone> phones) { }