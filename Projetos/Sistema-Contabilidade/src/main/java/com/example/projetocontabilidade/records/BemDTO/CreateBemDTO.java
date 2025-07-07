package com.example.projetocontabilidade.records.BemDTO;

import java.math.BigDecimal;

public record CreateBemDTO(
        String nome,
        BigDecimal preco,
        Integer quantidade
) {
}
