package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.*;
import com.example.projetocontabilidade.records.CompraDTO.CreateCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.projetocontabilidade.records.CompraDTO.GetCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;
import com.example.projetocontabilidade.repository.CompraRepository;
import com.example.projetocontabilidade.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {
    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;
    private final FornecedorRepository fornecedorRepository;
    private final ICMSService icmsService;
    private final CaixaService caixaService;
    private final PatrimonioService patrimonioService;
    private final APagarCompraService aPagarService;

    public CompraService(CompraRepository compraRepository,
                         ProdutoService produtoService,
                         FornecedorRepository fornecedorRepository,
                         ICMSService icmsService, CaixaService caixaService,
                         PatrimonioService patrimonioService,
                         APagarCompraService aPagarService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
        this.fornecedorRepository = fornecedorRepository;
        this.icmsService = icmsService;
        this.caixaService = caixaService;
        this.patrimonioService = patrimonioService;
        this.aPagarService = aPagarService;
    }

    @Transactional
    public Compra save(CreateCompraDTO compraDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(compraDTO.fornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Produto produto;

        if (compraDTO.isNovoProduto()) {
            CreateProdutoDTO produtoDTO = new CreateProdutoDTO(
                    compraDTO.produto().nome(),
                    compraDTO.produto().precoCompra(),
                    compraDTO.produto().precoVenda(),
                    compraDTO.quantidade()
            );
            produto = produtoService.save(produtoDTO);
        } else {
            Produto produtoExistente = produtoService.getProdutoEntityById(compraDTO.produtoExistenteId());

            String nome = (compraDTO.produto().nome() != null && !compraDTO.produto().nome().isEmpty())
                    ? compraDTO.produto().nome()
                    : produtoExistente.getNome();

            BigDecimal precoCompra = compraDTO.produtoExistentePrecoCompra() != null
                    ? compraDTO.produtoExistentePrecoCompra()
                    : compraDTO.produto().precoCompra();

            BigDecimal precoVenda = compraDTO.produtoExistentePrecoVenda() != null
                    ? compraDTO.produtoExistentePrecoVenda()
                    : compraDTO.produto().precoVenda();

            UpdateProdutoDTO updateProdutoDTO = new UpdateProdutoDTO(
                    nome,
                    precoCompra,
                    precoVenda,
                    produtoExistente.getQuantidade() + compraDTO.quantidade()
            );

            produtoService.update(compraDTO.produtoExistenteId(), updateProdutoDTO);
            produto = produtoService.getProdutoEntityById(compraDTO.produtoExistenteId());
        }

        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setProduto(produto);
        compra.setDataCompra(compraDTO.dataCompra());
        compra.setValorTotal(compraDTO.valorTotal());
        compra.setQuantidade(compraDTO.quantidade());

        // Tratamento do tipo de pagamento
        TipoPagamento tipoPagamento = TipoPagamento.valueOf(compraDTO.tipoPagamento().toUpperCase());
        compra.setTipoPagamento(tipoPagamento);

        Compra compraSalva = compraRepository.save(compra);

        // Se for a prazo, armazena as parcelas
        if (tipoPagamento == TipoPagamento.APRAZO) {
            if (compraDTO.parcelas() == null || compraDTO.parcelas() <= 0) {
                throw new IllegalArgumentException("Número de parcelas inválido para compra a prazo");
            }

            // Criar parcelas
            List<ParcelaCompra> parcelas = new ArrayList<>();
            BigDecimal valorParcela = compraDTO.valorTotal().divide(
                    new BigDecimal(compraDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );

            LocalDate dataVencimento = LocalDate.now().plusMonths(1);

            for (int i = 1; i <= compraDTO.parcelas(); i++) {
                ParcelaCompra parcela = new ParcelaCompra();
                parcela.setNumero(i);
                parcela.setValor(valorParcela);
                parcela.setDataVencimento(dataVencimento.plusMonths(i-1));
                parcela.setPaga(i == 1);
                parcela.setCompra(compra);
                parcelas.add(parcela);
            }

            compra.setParcelas(parcelas);
        }

        icmsService.compensarDebitoELancarCredito(compraDTO.fornecedorId(), compraDTO.valorTotal());

        patrimonioService.adicionarAoPatrimonio(compraDTO.valorTotal());

        if (tipoPagamento == TipoPagamento.AVISTA) {
            caixaService.subtrairDoCaixa(compraDTO.valorTotal());
        } else {
            BigDecimal valorParcela = compraDTO.valorTotal().divide(
                    new BigDecimal(compraDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );
            caixaService.subtrairDoCaixa(valorParcela);
        }

        return compraSalva;
    }

    public List<GetCompraDTO> getAllCompras() {
        return compraRepository.findAll().stream()
                .map(compra -> new GetCompraDTO(
                        compra.getFornecedor(),
                        compra.getDataCompra(),
                        compra.getProduto().getNome(),
                        compra.getValorTotal(),
                        compra.getQuantidade()
                ))
                .toList();
    }
}