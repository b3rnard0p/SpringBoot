package com.example.pratica2.controller;

import com.example.pratica2.models.Produto;
import com.example.pratica2.records.ProdutoDTO.CreateProdutoDTO;
import com.example.pratica2.records.ProdutoDTO.GetProdutoDTO;
import com.example.pratica2.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.pratica2.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        Map<String, Map<String, List<GetProdutoDTO>>> ProdutosByStatus = produtoService.getProdutosByStatus();
        model.addAttribute("ProdutosByStatus", ProdutosByStatus);
        return "Admin/List";
    }

    @GetMapping("/{id}")
    public String Detail(@PathVariable Long id, Model model) {
        GetProdutoDTO produto = produtoService.getProdutoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        model.addAttribute("produto", produto);
        return "Cliente/Detail";
    }

    @GetMapping
    public String showFormInsert(Model model) {
        model.addAttribute("produto", new Produto());
        return "Admin/Insert";
    }

    @PostMapping
    public String create(@ModelAttribute CreateProdutoDTO produto) {
        System.out.println(produto.toString());
        produtoService.createProduto(produto);
        return "redirect:/produto/list";
    }

    @GetMapping("/editar/{id}")
    public String showFormEdit(@PathVariable Long id, Model model) {
        GetProdutoDTO produto = produtoService.getProdutoById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        model.addAttribute("produto", produto);
        return "Admin/Edit";
    }


    @PutMapping("/editar/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UpdateProdutoDTO produtoDTO) {
        produtoService.updateProduto(id, produtoDTO);
        return "redirect:/produto/list";
    }
}