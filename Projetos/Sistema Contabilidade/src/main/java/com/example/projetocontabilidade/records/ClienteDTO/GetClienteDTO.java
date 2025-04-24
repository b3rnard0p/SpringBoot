package com.example.projetocontabilidade.records.ClienteDTO;

import com.example.projetocontabilidade.model.Estado;

public record GetClienteDTO (Long id, String nome, String cpf, String cidade, Estado estado){
}
