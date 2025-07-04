package com.example.projetocontabilidade.records.ClienteDTO;

import com.example.projetocontabilidade.model.Estado;

public record CreateClienteDTO (String nome, String cpf, String cidade, Estado estado){
}
