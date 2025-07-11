package com.example.projetocontabilidade.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.projetocontabilidade.model.Cliente;
import com.example.projetocontabilidade.model.Fornecedor;
import com.example.projetocontabilidade.model.ICMS;
import com.example.projetocontabilidade.repository.ClienteRepository;
import com.example.projetocontabilidade.repository.FornecedorRepository;
import com.example.projetocontabilidade.repository.ICMSRepository;

import jakarta.transaction.Transactional;

@Service
public class ICMSService {
    private final ICMSRepository icmsRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ClienteRepository clienteRepository;
    private final CaixaService caixaService;

    public ICMSService(ICMSRepository icmsRepository,
                       FornecedorRepository fornecedorRepository,
                       ClienteRepository clienteRepository,
                       CaixaService caixaService) {
        this.icmsRepository = icmsRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.clienteRepository = clienteRepository;
        this.caixaService = caixaService;
    }

    @Transactional
    public void debitar(Long clienteId, BigDecimal valorBase) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        BigDecimal aliquota = cliente.getEstado().getAliquotaAsBigDecimal();
        BigDecimal valorICMS = valorBase.multiply(aliquota);

        ICMS icms = getOrCreateICMS();
        BigDecimal creditoUsado = icms.getCredito().min(valorICMS);
        icms.setCredito(icms.getCredito().subtract(creditoUsado));

        BigDecimal valorExcedente = valorICMS.subtract(creditoUsado);
        if (valorExcedente.compareTo(BigDecimal.ZERO) > 0) {
            icms.setDebito(icms.getDebito().add(valorExcedente));
        }

        icmsRepository.save(icms);
    }

    @Transactional
    public void compensarDebitoELancarCredito(Long fornecedorId, BigDecimal valorBase) {
        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        BigDecimal aliquota = fornecedor.getEstado().getAliquotaAsBigDecimal();
        BigDecimal valorICMS = valorBase.multiply(aliquota);

        ICMS icms = getOrCreateICMS();

        BigDecimal debitoCompensado = icms.getDebito().min(valorICMS);
        icms.setDebito(icms.getDebito().subtract(debitoCompensado));

        BigDecimal creditoAdicional = valorICMS.subtract(debitoCompensado);
        if (creditoAdicional.compareTo(BigDecimal.ZERO) > 0) {
            icms.setCredito(icms.getCredito().add(creditoAdicional));
        }

        icmsRepository.save(icms);
    }

    @Transactional
    public BigDecimal pagarDebitoICMS(BigDecimal valorPagamento) {
        ICMS icms = getOrCreateICMS();
        BigDecimal debitoAtual = icms.getDebito();
        BigDecimal valorPago = valorPagamento.min(debitoAtual);
        if (valorPago.compareTo(BigDecimal.ZERO) > 0) {
            icms.setDebito(debitoAtual.subtract(valorPago));
            icmsRepository.save(icms);
            caixaService.subtrairDoCaixa(valorPago);
        }
        return valorPago;
    }

    private ICMS getOrCreateICMS() {
        return icmsRepository.findById(1L).orElseGet(() -> {
            ICMS novo = new ICMS();
            novo.setCredito(BigDecimal.ZERO);
            novo.setDebito(BigDecimal.ZERO);
            return icmsRepository.save(novo);
        });
    }

    public BigDecimal getSaldoCredito() {
        return icmsRepository.findById(1L)
                .map(ICMS::getCredito)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getSaldoDebito() {
        return icmsRepository.findById(1L)
                .map(ICMS::getDebito)
                .orElse(BigDecimal.ZERO);
    }
}