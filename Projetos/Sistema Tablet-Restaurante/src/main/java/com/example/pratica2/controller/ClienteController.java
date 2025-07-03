package com.example.pratica2.controller;

import com.example.pratica2.records.ProdutoDTO.GetProdutoDTO;
import com.example.pratica2.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ProdutoService produtoService;

    public ClienteController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/list")
    public String showClientList(HttpSession session, Model model) {
        Map<String, Map<String, List<GetProdutoDTO>>> produtosByStatus = produtoService.getProdutosByStatus();
        model.addAttribute("ProdutosByStatus", Map.of(
                "disponiveis", produtosByStatus.get("disponiveis")
        ));
        return "Cliente/List";
    }
}