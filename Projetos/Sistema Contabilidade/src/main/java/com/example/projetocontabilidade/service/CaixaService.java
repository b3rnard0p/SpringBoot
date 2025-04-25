package com.example.projetocontabilidade.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CaixaService {

    private BigDecimal saldo = BigDecimal.ZERO;

    @Transactional
    public void adicionarAoCaixa(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        this.saldo = this.saldo.add(valor);
    }

    @Transactional
    public void subtrairDoCaixa(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    // Opcional: método para definir saldo diretamente (se necessário)
    @Transactional
    public void setSaldo(BigDecimal novoSaldo) {
        if (novoSaldo == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }
        this.saldo = novoSaldo;
    }
}
