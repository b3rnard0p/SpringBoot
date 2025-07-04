package com.example.projetocontabilidade.records.ProdutoDTO;
import java.math.BigDecimal;

public record GetProdutoDTO(Long id, String nome, BigDecimal precoCompra, BigDecimal precoVenda, int quantidade) {}