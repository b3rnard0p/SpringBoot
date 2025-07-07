package com.example.projetocontabilidade.records.CompraBensDTO;

import com.example.projetocontabilidade.records.BemDTO.CreateBemDTO;

import java.math.BigDecimal;

import java.time.LocalDate;

public record CreateCompraBensDTO(
        boolean novoBem,
        LocalDate dataCompra,
        BigDecimal valorTotal,
        Integer quantidade,
        CreateBemDTO bem,
        Long bemExistenteId,
        BigDecimal bemExistentePreco,
        String tipoPagamento,
        Integer parcelas
) {
}
