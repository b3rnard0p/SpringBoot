package com.example.projetocontabilidade.records.BemDTO;

import java.math.BigDecimal;

public record UpdateBemDTO(
        String nome,
        BigDecimal preco,
        Integer quantidade
) {
}
