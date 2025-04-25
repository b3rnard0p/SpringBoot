package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.records.VendaDTO.CreateVendaDTO;
import com.example.projetocontabilidade.service.ClienteService;
import com.example.projetocontabilidade.service.ProdutoService;
import com.example.projetocontabilidade.service.VendaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService, ClienteService clienteService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
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
}