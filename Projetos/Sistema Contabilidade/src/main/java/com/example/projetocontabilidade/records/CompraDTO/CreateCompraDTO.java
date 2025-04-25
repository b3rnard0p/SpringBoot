package com.example.projetocontabilidade.records.CompraDTO;

import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCompraDTO(
        Long fornecedorId,
        LocalDate dataCompra,
        BigDecimal valorTotal,
        Integer quantidade,
        CreateProdutoDTO produto,
        Long produtoExistenteId,  // Novo campo para ID do produto existente
        boolean isNovoProduto,
        BigDecimal produtoExistentePrecoCompra,
        BigDecimal produtoExistentePrecoVenda
) {}