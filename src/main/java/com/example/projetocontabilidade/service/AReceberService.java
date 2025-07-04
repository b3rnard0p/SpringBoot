package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.ParcelaVenda;
import com.example.projetocontabilidade.repository.ParcelaVendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AReceberService {

    private final ParcelaVendaRepository parcelaVendaRepository;
    private final CaixaService caixaService;

    public AReceberService(ParcelaVendaRepository parcelaVendaRepository,
                           CaixaService caixaService) {
        this.parcelaVendaRepository = parcelaVendaRepository;
        this.caixaService = caixaService;
    }

    /**
     * Marca a próxima parcela não paga como recebida,
     * atualiza o caixa e persiste a alteração.
     */
    @Transactional
    public void receberParcela(Long vendaId) {
        List<ParcelaVenda> pendentes = parcelaVendaRepository
                .findByVendaIdAndPagaFalseOrderByNumeroAsc(vendaId);

        if (pendentes.isEmpty()) {
            throw new IllegalStateException("Todas as parcelas já foram recebidas ou venda não encontrada.");
        }

        ParcelaVenda parcela = pendentes.get(0);
        parcela.setPaga(true);
        parcelaVendaRepository.save(parcela);

        // Atualiza o caixa com o valor recebido
        caixaService.adicionarAoCaixa(parcela.getValor());
    }

    /**
     * Retorna o somatório dos valores de todas as parcelas ainda não recebidas.
     */
    public BigDecimal getSaldoDevedor(Long vendaId) {
        List<ParcelaVenda> pendentes = parcelaVendaRepository
                .findByVendaIdAndPagaFalseOrderByNumeroAsc(vendaId);

        return pendentes.stream()
                .map(ParcelaVenda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Total geral a receber, somando todas as parcelas não pagas de todas as vendas.
     */
    public BigDecimal getTotalAReceber() {
        return parcelaVendaRepository.findAll().stream()
                .filter(p -> !p.isPaga())
                .map(ParcelaVenda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
