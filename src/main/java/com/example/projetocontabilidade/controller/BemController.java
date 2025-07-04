package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.records.ProdutoDTO.GetProdutoDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.projetocontabilidade.service.BemService;
import com.example.projetocontabilidade.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bens")
public class BemController {
    private final BemService bemService;

    public BemController(BemService bemService) {
        this.bemService = bemService;
    }

    @GetMapping
    public String listBens(Model model) {
        model.addAttribute("title", "Lista de Bens");
        model.addAttribute("bens", bemService.getAllBens());
        return "Bens/List";
    }
}
