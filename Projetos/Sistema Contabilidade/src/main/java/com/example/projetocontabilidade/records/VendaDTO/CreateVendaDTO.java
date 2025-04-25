package com.example.projetocontabilidade.records.VendaDTO;


import java.math.BigDecimal;

public record CreateVendaDTO(
        Long clienteId,
        Long produtoId,
        Integer quantidade,
        BigDecimal total,
        String tipoPagamento,
        Integer parcelas // Adicione este campo
) {}