package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.ParcelaCompra;
import com.example.projetocontabilidade.repository.ParcelaCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class APagarCompraService {

    private final ParcelaCompraRepository parcelaCompraRepository;
    private final CaixaService caixaService;

    public APagarCompraService(ParcelaCompraRepository parcelaCompraRepository, CaixaService caixaService) {
        this.parcelaCompraRepository = parcelaCompraRepository;
        this.caixaService = caixaService;
    }

    @Transactional
    public void pagarParcela(Long compraId) {
        List<ParcelaCompra> parcelasPendentes = parcelaCompraRepository
                .findByCompraIdAndPagaFalseOrderByNumeroAsc(compraId);

        if (parcelasPendentes.isEmpty()) {
            throw new IllegalStateException("Todas as parcelas já foram pagas ou compra não encontrada.");
        }

        ParcelaCompra parcela = parcelasPendentes.get(0);
        parcela.setPaga(true);
        parcelaCompraRepository.save(parcela);

        caixaService.subtrairDoCaixa(parcela.getValor());
    }

    public BigDecimal getSaldoDevedor(Long compraId) {
        List<ParcelaCompra> parcelasPendentes = parcelaCompraRepository
                .findByCompraIdAndPagaFalseOrderByNumeroAsc(compraId);

        return parcelasPendentes.stream()
                .map(ParcelaCompra::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalAPagar() {
        return parcelaCompraRepository.findAll().stream()
                .filter(p -> !p.isPaga())
                .map(ParcelaCompra::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

