package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Patrimonio;
import com.example.projetocontabilidade.repository.PatrimonioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PatrimonioService {
    private final PatrimonioRepository patrimonioRepository;

    public PatrimonioService(PatrimonioRepository patrimonioRepository) {
        this.patrimonioRepository = patrimonioRepository;
    }

    @Transactional
    public void adicionarAoPatrimonio(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        Patrimonio patrimonio = getOrCreatePatrimonio();
        patrimonio.setSaldo(patrimonio.getSaldo().add(valor));
        patrimonioRepository.save(patrimonio);
    }

    @Transactional
    public void subtrairDoPatrimonio(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        Patrimonio patrimonio = getOrCreatePatrimonio();
        patrimonio.setSaldo(patrimonio.getSaldo().subtract(valor));
        patrimonioRepository.save(patrimonio);
    }

    public BigDecimal getSaldo() {
        return getOrCreatePatrimonio().getSaldo();
    }

    private Patrimonio getOrCreatePatrimonio() {
        return patrimonioRepository.findById(1L).orElseGet(() -> {
            Patrimonio novoPatrimonio = new Patrimonio();
            novoPatrimonio.setSaldo(BigDecimal.ZERO);
            return patrimonioRepository.save(novoPatrimonio);
        });
    }
}