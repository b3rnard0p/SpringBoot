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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ICMSService icmsService;
    private final CaixaService caixaService;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoRepository produtoRepository,
                        ClienteRepository clienteRepository,
                        ICMSService icmsService, CaixaService caixaService) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.icmsService = icmsService;
        this.caixaService = caixaService;
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

        // Atualiza estoque
        int novaQuantidade = produto.getQuantidade() - vendaDTO.quantidade();
        produto.setQuantidade(novaQuantidade);
        produtoRepository.save(produto);

        // Cria venda
        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setCliente(cliente);
        venda.setProduto(produto);
        venda.setQuantidade(vendaDTO.quantidade());
        venda.setTotal(vendaDTO.total());

        // Tratamento do tipo de pagamento
        Venda.TipoPagamento tipoPagamento = Venda.TipoPagamento.valueOf(vendaDTO.tipoPagamento().toUpperCase());
        venda.setTipoPagamento(tipoPagamento);

        // Se for a prazo, armazena as parcelas
        if (tipoPagamento == Venda.TipoPagamento.APRAZO) {
            if (vendaDTO.parcelas() == null || vendaDTO.parcelas() <= 0) {
                throw new IllegalArgumentException("Número de parcelas inválido para venda a prazo");
            }
            venda.setParcelas(vendaDTO.parcelas());
        }

        Venda vendaSalva = vendaRepository.save(venda);

        // ICMS - sempre sobre o valor total
        BigDecimal valorVenda = produto.getPrecoVenda().multiply(new BigDecimal(vendaDTO.quantidade()));
        icmsService.debitar(vendaDTO.clienteId(), valorVenda);

        // Caixa - lógica diferenciada para à vista vs a prazo
        if (tipoPagamento == Venda.TipoPagamento.AVISTA) {
            // À vista: adiciona o valor total
            caixaService.adicionarAoCaixa(vendaDTO.total());
        } else {
            // A prazo: adiciona apenas o valor de uma parcela
            BigDecimal valorParcela = vendaDTO.total().divide(
                    new BigDecimal(vendaDTO.parcelas()),
                    2,
                    RoundingMode.HALF_UP
            );
            caixaService.adicionarAoCaixa(valorParcela);

            // Opcional: criar registros de recebimentos futuros
            // criarParcelas(vendaSalva, valorParcela);
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