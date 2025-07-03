package com.example.pratica2.records.ItensComandaDTO;

public record CreateItensComandaDTO(Long produtoId,
        String produtoNome,
        Double produtoPreco,
        Integer quantidade,
        Double subtotal
) {
}
