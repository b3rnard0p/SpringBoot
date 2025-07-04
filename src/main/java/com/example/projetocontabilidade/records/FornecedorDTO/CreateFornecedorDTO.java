package com.example.projetocontabilidade.records.FornecedorDTO;

import com.example.projetocontabilidade.model.Estado;

public record CreateFornecedorDTO(String nome, String cnpj, String cidade, Estado estado) {
}
