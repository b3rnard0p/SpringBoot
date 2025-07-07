package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.*;
import com.example.projetocontabilidade.records.VendaDTO.CreateVendaDTO;
import com.example.projetocontabilidade.records.VendaDTO.GetVendaDTO;
import com.example.projetocontabilidade.repository.VendaRepository;
import com.example.projetocontabilidade.repository.ProdutoRepository;
import com.example.projetocontabilidade.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ICMSService icmsService;
    private final CaixaService caixaService;
    private  final PatrimonioService patrimonioService;
    private final AReceberService aReceberService;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoRepository produtoRepository,
                        ClienteRepository clienteRepository,
                        ICMSService icmsService, CaixaService caixaService,
                        PatrimonioService patrimonioService,
                        AReceberService aReceberService) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.icmsService = icmsService;
        this.caixaService = caixaService;
        this.patrimonioService = patrimonioService;
        this.aReceberService = aReceberService;
    }

    @Transactional
    public Venda save(CreateVendaDTO vendaDTO) {
        Cliente cliente = clienteRepository.findById(vendaDTO.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));

        Produto produto = produtoRepository.findById(vendaDTO.produtoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));

        if (produto.getQuantidade() < vendaDTO.quantidade()) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }
        BigDecimal custoTotal = produto.getPrecoCompra().multiply(new BigDecimal(vendaDTO.quantidade()));

        int novaQuantidade = produto.getQuantidade() - vendaDTO.quantidade();
        produto.setQuantidade(novaQuantidade);
        produtoRepository.save(produto);

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setCliente(cliente);
        venda.setProduto(produto);
        venda.setQuantidade(vendaDTO.quantidade());
        venda.setTotal(vendaDTO.total());

        TipoPagamento tipoPagamento = TipoPagamento.valueOf(vendaDTO.tipoPagamento().toUpperCase());
        venda.setTipoPagamento(tipoPagamento);

        Venda vendaSalva = vendaRepository.save(venda);

        if (tipoPagamento == TipoPagamento.APRAZO) {
            if (vendaDTO.parcelas() == null || vendaDTO.parcelas() <= 0) {
                throw new IllegalArgumentException("Número de parcelas inválido para venda a prazo");
            }
            List<ParcelaVenda> parcelas = new ArrayList<>();
            BigDecimal valorParcela = vendaDTO.total().divide(
                    new BigDecimal(vendaDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );

            LocalDate dataVencimento = LocalDate.now().plusMonths(1);

            for (int i = 1; i <= vendaDTO.parcelas(); i++) {
                ParcelaVenda parcela = new ParcelaVenda();
                parcela.setNumero(i);
                parcela.setValor(valorParcela);
                parcela.setDataVencimento(dataVencimento.plusMonths(i-1));
                parcela.setPaga(i == 1);
                parcela.setVenda(venda);
                parcelas.add(parcela);
            }

            venda.setParcelas(parcelas);
        }


        BigDecimal valorVenda = produto.getPrecoVenda().multiply(new BigDecimal(vendaDTO.quantidade()));
        icmsService.debitar(vendaDTO.clienteId(), valorVenda);

        if (tipoPagamento == TipoPagamento.AVISTA) {
            caixaService.adicionarAoCaixa(vendaDTO.total());
        } else {
            BigDecimal valorParcela = vendaDTO.total().divide(
                    new BigDecimal(vendaDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );
            caixaService.adicionarAoCaixa(valorParcela);
        }

        patrimonioService.subtrairDoPatrimonio(custoTotal);

        return vendaSalva;
    }

    public List<GetVendaDTO> getAllVendas() {
        return vendaRepository.findAll().stream()
                .map(venda -> new GetVendaDTO(
                        venda.getData(),
                        venda.getCliente(),
                        venda.getTotal(),
                        venda.getProduto().getNome(),
                        venda.getQuantidade(),
                        venda.getTipoPagamento().name()
                )).toList();
    }
}