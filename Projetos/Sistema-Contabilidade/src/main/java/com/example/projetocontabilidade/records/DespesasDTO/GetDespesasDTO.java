package com.example.projetocontabilidade.records.DespesasDTO;

import java.math.BigDecimal;

public record GetDespesasDTO(
        Long id,
        String nome,
        BigDecimal valor,
        boolean pago
) {
}