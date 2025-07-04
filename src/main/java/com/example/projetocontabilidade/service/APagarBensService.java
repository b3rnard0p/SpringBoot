package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.ParcelaBens;
import com.example.projetocontabilidade.repository.ParcelaBensRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class APagarBensService {

    private final ParcelaBensRepository parcelaBensRepository;
    private final CaixaService caixaService;

    public APagarBensService(ParcelaBensRepository parcelaBensRepository,
                             CaixaService caixaService) {
        this.parcelaBensRepository = parcelaBensRepository;
        this.caixaService = caixaService;
    }

    /**
     * Marca a próxima parcela não paga como quitada,
     * atualiza o caixa e persiste a alteração.
     */
    @Transactional
    public void pagarParcela(Long compraBensId) {
        List<ParcelaBens> pendentes = parcelaBensRepository
                .findByCompraBensIdAndPagaFalseOrderByNumeroAsc(compraBensId);

        if (pendentes.isEmpty()) {
            throw new IllegalStateException("Todas as parcelas já foram pagas ou compra de bens não encontrada.");
        }

        ParcelaBens parcela = pendentes.get(0);
        parcela.setPaga(true);
        parcelaBensRepository.save(parcela);

        // Subtrai do caixa o valor da parcela paga
        caixaService.subtrairDoCaixa(parcela.getValor());
    }

    /**
     * Retorna o somatório dos valores de todas as parcelas ainda não pagas para uma compra de bens.
     */
    public BigDecimal getSaldoDevedor(Long compraBensId) {
        List<ParcelaBens> pendentes = parcelaBensRepository
                .findByCompraBensIdAndPagaFalseOrderByNumeroAsc(compraBensId);

        return pendentes.stream()
                .map(ParcelaBens::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Total geral a pagar, somando todas as parcelas não pagas de todas as compras de bens.
     */
    public BigDecimal getTotalAPagar() {
        return parcelaBensRepository.findAll().stream()
                .filter(p -> !p.isPaga())
                .map(ParcelaBens::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
