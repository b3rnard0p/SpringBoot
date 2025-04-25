package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Compra;
import com.example.projetocontabilidade.model.Fornecedor;
import com.example.projetocontabilidade.model.Produto;
import com.example.projetocontabilidade.records.CompraDTO.CreateCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.projetocontabilidade.records.CompraDTO.GetCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;
import com.example.projetocontabilidade.repository.CompraRepository;
import com.example.projetocontabilidade.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {
    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;
    private final FornecedorRepository fornecedorRepository;
    private final ICMSService icmsService;
    private final CaixaService caixaService;

    public CompraService(CompraRepository compraRepository,
                         ProdutoService produtoService,
                         FornecedorRepository fornecedorRepository,
                         ICMSService icmsService, CaixaService caixaService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
        this.fornecedorRepository = fornecedorRepository;
        this.icmsService = icmsService;
        this.caixaService = caixaService;
    }

    @Transactional
    public Compra save(CreateCompraDTO compraDTO) {
        // Validações iniciais
        Fornecedor fornecedor = fornecedorRepository.findById(compraDTO.fornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Produto produto;

        if (compraDTO.isNovoProduto()) {
            // Lógica para novo produto
            CreateProdutoDTO produtoDTO = new CreateProdutoDTO(
                    compraDTO.produto().nome(),
                    compraDTO.produto().precoCompra(),
                    compraDTO.produto().precoVenda(),
                    compraDTO.quantidade()
            );
            produto = produtoService.save(produtoDTO);
        } else {
            // Lógica para produto existente
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

        // Cria e salva a compra
        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setProduto(produto);
        compra.setDataCompra(compraDTO.dataCompra());
        compra.setValorTotal(compraDTO.valorTotal());
        compra.setQuantidade(compraDTO.quantidade());

        Compra compraSalva = compraRepository.save(compra);

        // ICMS - mantém a chamada original que estava funcionando
        icmsService.compensarDebitoELancarCredito(compraDTO.fornecedorId(), compraDTO.valorTotal());

        // Caixa - subtrai o valor TOTAL (incluindo ICMS)
        // Isso porque o ICMS é compensação fiscal, não saída de caixa
        caixaService.subtrairDoCaixa(compraDTO.valorTotal());

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