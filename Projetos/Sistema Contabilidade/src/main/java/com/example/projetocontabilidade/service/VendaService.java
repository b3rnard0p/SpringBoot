package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Cliente;
import com.example.projetocontabilidade.model.Produto;
import com.example.projetocontabilidade.model.Venda;
import com.example.projetocontabilidade.records.VendaDTO.CreateVendaDTO;
import com.example.projetocontabilidade.records.VendaDTO.GetVendaDTO;
import com.example.projetocontabilidade.repository.VendaRepository;
import com.example.projetocontabilidade.repository.ProdutoRepository;
import com.example.projetocontabilidade.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ICMSService icmsService;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoRepository produtoRepository,
                        ClienteRepository clienteRepository,
                        ICMSService icmsService) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.icmsService = icmsService;
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

        int novaQuantidade = produto.getQuantidade() - vendaDTO.quantidade();
        produto.setQuantidade(novaQuantidade);
        produtoRepository.save(produto);

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setCliente(cliente);
        venda.setProduto(produto);
        venda.setQuantidade(vendaDTO.quantidade());
        venda.setTotal(vendaDTO.total());
        venda.setTipoPagamento(Venda.TipoPagamento.valueOf(vendaDTO.tipoPagamento().toUpperCase()));

        Venda vendaSalva = vendaRepository.save(venda);

        BigDecimal valorVenda = produto.getPrecoVenda().multiply(new BigDecimal(vendaDTO.quantidade()));
        icmsService.debitar(vendaDTO.clienteId(), valorVenda);

        if (icmsService.getSaldoCredito().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("ATENÇÃO: Saldo de ICMS negativo");
        }

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