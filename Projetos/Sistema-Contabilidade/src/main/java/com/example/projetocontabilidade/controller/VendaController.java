package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.model.ParcelaVenda;
import com.example.projetocontabilidade.records.VendaDTO.CreateVendaDTO;
import com.example.projetocontabilidade.repository.ParcelaVendaRepository;
import com.example.projetocontabilidade.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final CaixaService caixaService;
    private final AReceberService aReceberService;
    private final ParcelaVendaRepository parcelaRepository;

    public VendaController(VendaService vendaService, ClienteService clienteService, ProdutoService produtoService,
                           CaixaService caixaService, AReceberService aReceberService, ParcelaVendaRepository parcelaRepository) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
        this.caixaService = caixaService;
        this.aReceberService = aReceberService;
        this.parcelaRepository = parcelaRepository;
    }

    @GetMapping
    public String listVendas(Model model) {
        model.addAttribute("title", "Lista de Vendas");
        model.addAttribute("vendas", vendaService.getAllVendas());
        return "vendas/list";
    }

    @GetMapping("/nova")
    public String showForm(Model model) {
        model.addAttribute("clientes", clienteService.getAllClientes());
        model.addAttribute("produtos", produtoService.getAllProdutos());
        model.addAttribute("title", "Nova Venda");
        return "vendas/form";
    }

    @PostMapping("/salvar")
    public String saveVenda(@ModelAttribute CreateVendaDTO vendaDTO) {
        vendaService.save(vendaDTO);
        return "redirect:/vendas";
    }

    @PostMapping("/{vendaId}/parcelas/{parcelaId}/receber")
    public String receberParcela(
            @PathVariable Long vendaId,
            @PathVariable Long parcelaId
    ) {
        ParcelaVenda parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada"));

        if (!parcela.getVenda().getId().equals(vendaId)) {
            throw new IllegalArgumentException("Parcela não pertence à venda");
        }

        if (parcela.isPaga()) {
            throw new IllegalStateException("Parcela já está paga");
        }

        // Verifica se ainda há saldo a receber
        BigDecimal saldoDevedor = aReceberService.getSaldoDevedor(vendaId);
        if (saldoDevedor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Não há saldo devedor para esta venda");
        }

        aReceberService.receberParcela(vendaId);

        parcela.setPaga(true);
        parcelaRepository.save(parcela);

        return "redirect:/vendas/contas-a-receber";
    }

    @GetMapping("/contas-a-receber")
    public String showContasAReceber(Model model) {
        List<ParcelaVenda> parcelasPendentes = parcelaRepository.findByPagaFalseOrderByDataVencimento();
        model.addAttribute("parcelasPendentes", parcelasPendentes);
        return "vendas/contas-a-receber";
    }
}