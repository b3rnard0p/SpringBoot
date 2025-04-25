package com.example.projetocontabilidade.records.ProdutoDTO;

import java.math.BigDecimal;

public record CreateProdutoDTO (String nome, BigDecimal precoCompra, BigDecimal precoVenda, Integer quantidade){
}
