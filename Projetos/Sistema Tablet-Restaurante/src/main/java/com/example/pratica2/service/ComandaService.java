package com.example.pratica2.service;

import com.example.pratica2.models.*;
import com.example.pratica2.records.ComandaDTO.CloseComandaDTO;
import com.example.pratica2.records.ComandaDTO.ComandaResponseDTO;
import com.example.pratica2.records.ItensComandaDTO.AddItensComandaDTO;
import com.example.pratica2.records.ItensComandaDTO.CreateItensComandaDTO;
import com.example.pratica2.repository.ComandaRepository;
import com.example.pratica2.repository.ItensComandaRepository;
import com.example.pratica2.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComandaService {
    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private ItensComandaRepository itensComandaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Optional<ComandaResponseDTO> buscarComandaAtivaPorMesa(Tipo mesa) {
        return comandaRepository.findByMesaAndStatus(mesa, "ABERTA")
                .map(this::toComandaResponseDTO);
    }

    public ComandaResponseDTO fecharComanda(CloseComandaDTO dto) {
        Comanda comanda = comandaRepository.findById(dto.comandaId())
                .orElseThrow(() -> new EntityNotFoundException("Comanda não encontrada"));
        comanda.setStatus("FECHADA");

        Comanda comandaFechada = comandaRepository.save(comanda);

        gerarTicketComanda(comandaFechada);

        return toComandaResponseDTO(comandaFechada);
    }

private void gerarTicketComanda(Comanda comanda) {
    StringBuilder ticket = new StringBuilder();

    ticket.append("\n=== VAI PAGAR SAFADO ===\n");
    ticket.append("Mesa: ").append(comanda.getMesa()).append("\n");
    ticket.append("Data Abertura: ").append(comanda.getData()).append("\n");
    ticket.append("Status: ").append(comanda.getStatus()).append("\n");
    ticket.append("--------------------------------\n");

    ticket.append("ITENS:\n");
    double total = 0;
    for (ItensComanda item : comanda.getItensComanda()) {
        double subtotal = item.getProduto().getPreco() * item.getQuantidade();
        ticket.append(String.format("- %-20s x%-3d R$ %-6.2f = R$ %.2f\n",
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getProduto().getPreco(),
                subtotal));
        total += subtotal;
    }

    double taxaGraca = total * 0.10; // Calcula os 10% de graça
    double totalComGraça = total + taxaGraca;

    ticket.append("--------------------------------\n");
    ticket.append(String.format("SUB-TOTAL: R$ %.2f\n", total));
    ticket.append(String.format("TAXA DE GRAÇA (10%%): R$ %.2f\n", taxaGraca));
    ticket.append(String.format("TOTAL A PAGAR: R$ %.2f\n", totalComGraça));
    ticket.append("================================\n");

    System.out.println(ticket.toString());
}

    public ComandaResponseDTO adicionarItensAComandaExistente(Long comandaId, AddItensComandaDTO dto) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new EntityNotFoundException("Comanda não encontrada"));

        double valorTotalAdicional = 0;
        List<ItensComanda> novosItens = new ArrayList<>();

        for (CreateItensComandaDTO itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            ItensComanda novoItem = new ItensComanda();
            ItensComandaId id = new ItensComandaId();
            id.setComanda_id(comandaId);
            id.setProduto_id(itemDTO.produtoId());
            novoItem.setId(id);
            novoItem.setProduto(produto);
            novoItem.setComanda(comanda);
            novoItem.setQuantidade(itemDTO.quantidade());

            novosItens.add(novoItem);
            valorTotalAdicional += produto.getPreco() * itemDTO.quantidade();
        }

        for (ItensComanda novoItem : novosItens) {
            boolean itemJaExiste = false;

            for (ItensComanda itemExistente : comanda.getItensComanda()) {
                if (itemExistente.getProduto().getId().equals(novoItem.getProduto().getId())) {
                    itemExistente.setQuantidade(itemExistente.getQuantidade() + novoItem.getQuantidade());
                    itensComandaRepository.save(itemExistente);
                    itemJaExiste = true;
                    break;
                }
            }

            if (!itemJaExiste) {
                comanda.getItensComanda().add(novoItem);
                itensComandaRepository.save(novoItem);
            }
        }

        comanda.setValorTotal(comanda.getValorTotal() + valorTotalAdicional);
        return toComandaResponseDTO(comandaRepository.save(comanda));
    }

    public ComandaResponseDTO toComandaResponseDTO(Comanda comanda) {
        List<CreateItensComandaDTO> itensDTO = comanda.getItensComanda().stream()
                .map(item -> new CreateItensComandaDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getProduto().getPreco(),
                        item.getQuantidade(),
                        item.getProduto().getPreco() * item.getQuantidade()
                ))
                .collect(Collectors.toList());

        return new ComandaResponseDTO(
                comanda.getId(),
                comanda.getMesa(),
                comanda.getStatus(),
                comanda.getData(),
                comanda.getValorTotal(),
                itensDTO
        );
    }
}
