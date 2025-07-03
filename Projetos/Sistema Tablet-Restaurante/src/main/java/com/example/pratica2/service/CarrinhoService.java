package com.example.pratica2.service;

import com.example.pratica2.models.*;
import com.example.pratica2.records.ComandaDTO.ComandaResponseDTO;
import com.example.pratica2.records.ItensComandaDTO.AddItensComandaDTO;
import com.example.pratica2.records.ItensComandaDTO.CreateItensComandaDTO;
import com.example.pratica2.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarrinhoService {
    private final ProdutoRepository produtoRepository;
    private final ComandaRepository comandaRepository;
    private final ItensComandaRepository itensComandaRepository;
    private final UsuarioService usuarioService;
    private final ComandaService comandaService;
    private final Map<Long, Integer> itensCarrinho = new HashMap<>(); // Simulação em memória
    private String observacoes = "";

    public CarrinhoService(ProdutoRepository produtoRepository,
                           ComandaRepository comandaRepository,
                           ItensComandaRepository itensComandaRepository,
                           UsuarioService usuarioService,
                           ComandaService comandaService) {
        this.produtoRepository = produtoRepository;
        this.comandaRepository = comandaRepository;
        this.itensComandaRepository = itensComandaRepository;
        this.usuarioService = usuarioService;
        this.comandaService = comandaService;
    }

    public void adicionarProduto(Long produtoId) {
        itensCarrinho.merge(produtoId, 1, Integer::sum);
    }

    public void removerProduto(Long produtoId) {
        itensCarrinho.remove(produtoId);
    }

    public List<Produto> listarItens() {
        List<Produto> produtos = new ArrayList<>();
        for (Long produtoId : itensCarrinho.keySet()) {
            produtoRepository.findById(produtoId).ifPresent(produto -> {
                produtos.add(produto);
            });
        }
        return produtos;
    }

    public void atualizarQuantidades(Map<String, String> quantidades) {
        quantidades.forEach((key, value) -> {
            if (key.startsWith("quantidade_")) {
                Long produtoId = Long.parseLong(key.substring("quantidade_".length()));
                int quantidade = Integer.parseInt(value);

                if (quantidade > 0) {
                    itensCarrinho.put(produtoId, quantidade);
                } else {
                    itensCarrinho.remove(produtoId);
                }
            }
        });
    }

    public List<Map<String, Object>> getItensCarrinho() {
        return itensCarrinho.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    Produto produto = produtoRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                    itemMap.put("produto", produto);
                    itemMap.put("quantidade", entry.getValue());
                    itemMap.put("subtotal", produto.getPreco() * entry.getValue());

                    return itemMap;
                })
                .collect(Collectors.toList());
    }

    public void limparCarrinho() {
        itensCarrinho.clear();
    }

    public double calcularTotal() {
        return itensCarrinho.entrySet().stream()
                .mapToDouble(entry -> {
                    Produto produto = produtoRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                    return produto.getPreco() * entry.getValue();
                })
                .sum();
    }

    public void salvarObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public List<ItensCarrinho> obterItens() {
        return itensCarrinho.entrySet().stream()
                .map(entry -> {
                    Produto produto = produtoRepository.findById(entry.getKey())
                            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

                    ItensCarrinho item = new ItensCarrinho();
                    item.setProduto(produto);
                    item.setQuantidade(entry.getValue());

                    return item;
                })
                .collect(Collectors.toList());
    }

    public ComandaResponseDTO finalizarPedido(HttpSession session) {
        Tipo tipoMesa = usuarioService.obterTipoMesaAtual(session);
        List<ItensCarrinho> itensCarrinho = obterItens();

        if (itensCarrinho.isEmpty()) {
            throw new IllegalStateException("Carrinho vazio");
        }

        String ticket = gerarTicket(tipoMesa, itensCarrinho, observacoes);

        Optional<ComandaResponseDTO> comandaExistente = comandaService.buscarComandaAtivaPorMesa(tipoMesa);

        ComandaResponseDTO response;
        if (comandaExistente.isPresent()) {
            response = processarComandaExistente(comandaExistente.get(), itensCarrinho);
        } else {
            response = criarNovaComanda(tipoMesa, itensCarrinho);
        }

        enviarTicket(ticket);

        limparCarrinho();
        return response;
    }

    private String gerarTicket(Tipo mesa, List<ItensCarrinho> itens, String observacoes) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("=== PEDIDO ===\n");
        ticket.append("Mesa: ").append(mesa).append("\n");
        ticket.append("Data: ").append(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        ticket.append("----------------\n");

        for (ItensCarrinho item : itens) {
            ticket.append(String.format("- %-25s x%d\n",
                    item.getProduto().getNome(),
                    item.getQuantidade()));
        }

        if (observacoes != null && !observacoes.trim().isEmpty()) {
            ticket.append("----------------\n");
            ticket.append("OBSERVAÇÕES:\n");
            String obsFormatada = observacoes.replaceAll("(.{1,30})(?:\\s|$)", "$1\n");
            ticket.append(obsFormatada).append("\n");
        }

        ticket.append("========================");
        return ticket.toString();
    }

    private void enviarTicket(String ticket) {
        System.out.println(ticket);
    }

    private ComandaResponseDTO processarComandaExistente(ComandaResponseDTO comandaDTO, List<ItensCarrinho> itens) {
        List<CreateItensComandaDTO> itensParaAdicionar = converterItensParaDTO(itens);
        AddItensComandaDTO dto = new AddItensComandaDTO(itensParaAdicionar);
        return comandaService.adicionarItensAComandaExistente(comandaDTO.id(), dto);
    }

    private ComandaResponseDTO criarNovaComanda(Tipo mesa, List<ItensCarrinho> itens) {
        Comanda novaComanda = new Comanda();
        novaComanda.setMesa(mesa);
        novaComanda.setData(LocalDateTime.now());
        novaComanda.setStatus("ABERTA");

        List<ItensComanda> itensComanda = new ArrayList<>();
        double valorTotal = 0;

        for (ItensCarrinho item : itens) {
            ItensComanda itemComanda = criarItemComanda(item, novaComanda);
            itensComanda.add(itemComanda);
            valorTotal += item.getProduto().getPreco() * item.getQuantidade();
        }

        novaComanda.setItensComanda(itensComanda);
        novaComanda.setValorTotal(valorTotal);

        novaComanda = comandaRepository.save(novaComanda);
        itensComandaRepository.saveAll(itensComanda);

        return comandaService.toComandaResponseDTO(novaComanda);
    }

    private List<CreateItensComandaDTO> converterItensParaDTO(List<ItensCarrinho> itens) {
        return itens.stream()
                .map(item -> new CreateItensComandaDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getProduto().getPreco(),
                        item.getQuantidade(),
                        item.getProduto().getPreco() * item.getQuantidade()
                ))
                .collect(Collectors.toList());
    }

    private ItensComanda criarItemComanda(ItensCarrinho item, Comanda comanda) {
        ItensComanda itemComanda = new ItensComanda();
        ItensComandaId id = new ItensComandaId();
        id.setProduto_id(item.getProduto().getId());
        id.setComanda_id(comanda.getId());
        itemComanda.setId(id);
        itemComanda.setProduto(item.getProduto());
        itemComanda.setComanda(comanda);
        itemComanda.setQuantidade(item.getQuantidade());
        return itemComanda;
    }
}
