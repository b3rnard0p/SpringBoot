package com.example.projetocontabilidade.records.BemDTO;

import java.math.BigDecimal;

public record GetBemDTO(
        Long id,
        String nome,
        BigDecimal preco,
        int quantidade
) {
}
