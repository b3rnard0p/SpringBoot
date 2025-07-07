package com.example.projetocontabilidade.records.FornecedorDTO;

import com.example.projetocontabilidade.model.Estado;

public record UpdateFornecedorDTO(String nome, String cnpj, String cidade, Estado estado) {
}
