package com.example.projetocontabilidade.records.VendaDTO;


import java.math.BigDecimal;

public record CreateVendaDTO(Long clienteId, Long produtoId, int quantidade, BigDecimal total, String tipoPagamento) {}
