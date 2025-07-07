package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.*;
import com.example.projetocontabilidade.records.BemDTO.CreateBemDTO;
import com.example.projetocontabilidade.records.BemDTO.UpdateBemDTO;
import com.example.projetocontabilidade.records.CompraBensDTO.CreateCompraBensDTO;
import com.example.projetocontabilidade.records.CompraBensDTO.GetCompraBensDTO;
import com.example.projetocontabilidade.repository.CompraBensRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraBensService {
    private final CompraBensRepository compraBensRepository;
    private final BemService bemService;
    private final CaixaService caixaService;
    private final PatrimonioService patrimonioService;
    private final APagarBensService aPagarBensService;

    public CompraBensService(CompraBensRepository compraBensRepository,
                         BemService bemService,
                             CaixaService caixaService,
                             PatrimonioService patrimonioService,
                             APagarBensService aPagarBensService) {
        this.compraBensRepository = compraBensRepository;
        this.bemService = bemService;
        this.caixaService = caixaService;
        this.patrimonioService = patrimonioService;
        this.aPagarBensService = aPagarBensService;
    }

    @Transactional
    public CompraBens save(CreateCompraBensDTO compraDTO) {
        Bem bem;

        if (compraDTO.novoBem()) {
            CreateBemDTO bemDTO = new CreateBemDTO(
                    compraDTO.bem().nome(),
                    compraDTO.bem().preco(),
                    compraDTO.quantidade()
            );
            bem = bemService.save(bemDTO);
        } else {
            Bem bemExistente = bemService.getBemEntityById(compraDTO.bemExistenteId());

            String nome = (compraDTO.bem().nome() != null && !compraDTO.bem().nome().isEmpty())
                    ? compraDTO.bem().nome()
                    : bemExistente.getNome();

            BigDecimal preco = (compraDTO.bemExistentePreco() != null)
                    ? compraDTO.bemExistentePreco()
                    : compraDTO.bem().preco();

            UpdateBemDTO updateBemDTO = new UpdateBemDTO(
                    nome,
                    preco,
                    bemExistente.getQuantidade() + compraDTO.quantidade()
            );

            bemService.update(compraDTO.bemExistenteId(), updateBemDTO);
            bem = bemService.getBemEntityById(compraDTO.bemExistenteId());
        }

        CompraBens compra = new CompraBens();
        compra.setBem(bem);
        compra.setDataCompra(compraDTO.dataCompra());
        compra.setValorTotal(compraDTO.valorTotal());
        compra.setQuantidade(compraDTO.quantidade());

        // Tratamento do tipo de pagamento
        TipoPagamento tipoPagamento = TipoPagamento.valueOf(compraDTO.tipoPagamento().toUpperCase());
        compra.setTipoPagamento(tipoPagamento);

        CompraBens compraSalva = compraBensRepository.save(compra);

        // Se for a prazo, armazena as parcelas
        if (tipoPagamento == TipoPagamento.APRAZO) {
            if (compraDTO.parcelas() == null || compraDTO.parcelas() <= 0) {
                throw new IllegalArgumentException("Número de parcelas inválido para compra a prazo");
            }

            // Criar parcelas
            List<ParcelaBens> parcelas = new ArrayList<>();
            BigDecimal valorParcela = compraDTO.valorTotal().divide(
                    new BigDecimal(compraDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );

            LocalDate dataVencimento = LocalDate.now().plusMonths(1);

            for (int i = 1; i <= compraDTO.parcelas(); i++) {
                ParcelaBens parcela = new ParcelaBens();
                parcela.setNumero(i);
                parcela.setValor(valorParcela);
                parcela.setDataVencimento(dataVencimento.plusMonths(i-1));
                parcela.setPaga(i == 1); // Marca a primeira parcela como paga
                parcela.setCompraBens(compra);
                parcelas.add(parcela);
            }

            compra.setParcelas(parcelas);
        }

        // Adiciona ao patrimônio
        patrimonioService.adicionarAoPatrimonio(compraDTO.valorTotal());

        // Caixa - lógica diferenciada para à vista vs a prazo
        if (tipoPagamento == TipoPagamento.AVISTA) {
            // À vista: subtrai o valor total da compra do caixa
            caixaService.subtrairDoCaixa(compraDTO.valorTotal());
        } else {
            // A prazo: subtrai apenas o valor de uma parcela do caixa
            BigDecimal valorParcela = compraDTO.valorTotal().divide(
                    new BigDecimal(compraDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );
            caixaService.subtrairDoCaixa(valorParcela);
        }

        return compraSalva;
    }

    public List<GetCompraBensDTO> getAllCompras() {
        return compraBensRepository.findAll().stream()
                .map(compra -> new GetCompraBensDTO(
                        compra.getDataCompra(),
                        compra.getBem().getNome(),
                        compra.getValorTotal(),
                        compra.getQuantidade()
                ))
                .toList();
    }
}
