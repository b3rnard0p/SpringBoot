package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.repository.ParcelaBensRepository;
import com.example.projetocontabilidade.repository.ParcelaCompraRepository;
import com.example.projetocontabilidade.repository.ParcelaVendaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ParcelaService {

    private final ParcelaVendaRepository parcelaVendaRepository;
    private final ParcelaCompraRepository parcelaCompraRepository;
    private final ParcelaBensRepository parcelaBensRepository;

    public ParcelaService(ParcelaVendaRepository parcelaVendaRepository,
                          ParcelaCompraRepository parcelaCompraRepository,
                          ParcelaBensRepository parcelaBensRepository) {
        this.parcelaVendaRepository = parcelaVendaRepository;
        this.parcelaCompraRepository = parcelaCompraRepository;
        this.parcelaBensRepository = parcelaBensRepository;
    }

    public BigDecimal getTotalParcelasAReceber() {
        BigDecimal totalVendas = parcelaVendaRepository.sumValorByPagaFalse();
        return totalVendas != null ? totalVendas : BigDecimal.ZERO;
    }

    public BigDecimal getTotalParcelasAPagar() {
        BigDecimal totalCompras = parcelaCompraRepository.sumValorByPagaFalse();
        BigDecimal totalBens = parcelaBensRepository.sumValorByPagaFalse();

        BigDecimal totalComprasNotNull = totalCompras != null ? totalCompras : BigDecimal.ZERO;
        BigDecimal totalBensNotNull = totalBens != null ? totalBens : BigDecimal.ZERO;

        return totalComprasNotNull.add(totalBensNotNull);
    }
}