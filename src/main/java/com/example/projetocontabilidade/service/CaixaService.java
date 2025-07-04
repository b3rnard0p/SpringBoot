package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Caixa;
import com.example.projetocontabilidade.repository.CaixaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CaixaService {
    private final CaixaRepository caixaRepository;

    public CaixaService(CaixaRepository caixaRepository) {
        this.caixaRepository = caixaRepository;
    }

    @Transactional
    public void adicionarAoCaixa(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        Caixa caixa = getOrCreateCaixa();
        caixa.setSaldo(caixa.getSaldo().add(valor));
        caixaRepository.save(caixa);
    }

    @Transactional
    public void subtrairDoCaixa(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        Caixa caixa = getOrCreateCaixa();
        caixa.setSaldo(caixa.getSaldo().subtract(valor));
        caixaRepository.save(caixa);
    }

    public BigDecimal getSaldo() {
        return getOrCreateCaixa().getSaldo();
    }

    private Caixa getOrCreateCaixa() {
        return caixaRepository.findById(1L).orElseGet(() -> {
            Caixa novoCaixa = new Caixa();
            novoCaixa.setSaldo(BigDecimal.ZERO);
            return caixaRepository.save(novoCaixa);
        });
    }
}