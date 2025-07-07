package com.example.projetocontabilidade.records.DespesasDTO;

import java.math.BigDecimal;

public record CreateDespesasDTO(
        Long id,
        String nome,
        BigDecimal valor
) {
}
