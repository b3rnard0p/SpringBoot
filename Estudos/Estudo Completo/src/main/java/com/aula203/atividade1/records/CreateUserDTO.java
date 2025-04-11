package com.aula203.atividade1.records;

import com.aula203.atividade1.model.Phone;

import java.util.List;

public record CreateUserDTO(String nome, String email, String senha, List<CreatePhoneDTO> phones ) {
}