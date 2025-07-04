package com.example.projetocontabilidade.records.CompraBensDTO;

import com.example.projetocontabilidade.model.Fornecedor;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetCompraBensDTO(
        LocalDate dataCompra,
        String bemNome,
        BigDecimal valorTotal,
        int quantidade
) {
}
