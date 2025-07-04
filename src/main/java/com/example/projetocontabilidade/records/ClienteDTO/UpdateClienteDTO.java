package com.example.projetocontabilidade.records.ClienteDTO;

import com.example.projetocontabilidade.model.Estado;

public record UpdateClienteDTO(
        String nome,
        String cidade,
        Estado estado,
        String cpf
) {}