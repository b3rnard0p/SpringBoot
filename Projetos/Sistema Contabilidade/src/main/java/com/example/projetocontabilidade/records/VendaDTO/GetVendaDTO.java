package com.example.projetocontabilidade.records.VendaDTO;

import com.example.projetocontabilidade.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetVendaDTO(
        LocalDateTime data,
        Cliente cliente,
        BigDecimal total,
        String produtoNome,
        int quantidade,
        String tipoPagamento
) {}