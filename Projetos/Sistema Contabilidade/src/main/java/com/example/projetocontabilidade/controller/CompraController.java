package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.records.CompraDTO.CreateCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;
import com.example.projetocontabilidade.service.CompraService;
import com.example.projetocontabilidade.service.FornecedorService;
import com.example.projetocontabilidade.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;

    public CompraController(CompraService compraService, FornecedorService fornecedorService, ProdutoService produtoService) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listCompras(Model model) {
        model.addAttribute("title", "Lista de Compras");  // Corrigido o título
        model.addAttribute("produtos", compraService.getAllCompras());
        return "compras/list";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        CreateProdutoDTO produtoDTO = new CreateProdutoDTO("", BigDecimal.ZERO, BigDecimal.ZERO, 0);
        CreateCompraDTO compraDTO = new CreateCompraDTO(null, null, null, null, produtoDTO, null,
                false, null, null);
        model.addAttribute("produtos", produtoService.getAllProdutos());
        model.addAttribute("compraDTO", compraDTO);
        model.addAttribute("fornecedores", fornecedorService.getAllFornecedores());
        model.addAttribute("title", "Nova Compra");
        return "compras/insert";
    }


    @PostMapping("/salvar")
    public String saveCompra(@ModelAttribute CreateCompraDTO compraDTO) {
        compraService.save(compraDTO);
        return "redirect:/compras";
    }
}