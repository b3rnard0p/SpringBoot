package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.records.ProdutoDTO.GetProdutoDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.projetocontabilidade.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listProdutos(Model model) {
        model.addAttribute("title", "Lista de Produtos");
        model.addAttribute("produtos", produtoService.getAllProdutos());
        return "produtos/list";
    }

    @GetMapping("/editar/{id}")
    public String showFormEdit(@PathVariable Long id, Model model) {
        GetProdutoDTO produto = produtoService.getByIdProduto(id).orElseThrow(() -> new RuntimeException("produto não encontrado"));
        model.addAttribute("produto", produto);
        return "produtos/edit";
    }

    @PutMapping("/editar/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UpdateProdutoDTO produtoDTO) {
        produtoService.update(id, produtoDTO);
        return "redirect:/produtos";
    }
}