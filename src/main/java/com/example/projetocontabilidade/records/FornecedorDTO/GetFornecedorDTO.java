package com.example.projetocontabilidade.records.FornecedorDTO;

import com.example.projetocontabilidade.model.Estado;

public record GetFornecedorDTO(
        Long id,
        String nome,
        String cidade,
        Estado estado,
        String cnpj
) {}