package com.example.projetocontabilidade.records.CompraDTO;

import com.example.projetocontabilidade.model.Fornecedor;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetCompraDTO (
        Fornecedor fornecedor,
        LocalDate dataCompra,
        String produtoNome, // Agora recebe apenas o ID do produto existente
        BigDecimal valorTotal,
        int quantidade
){
}
