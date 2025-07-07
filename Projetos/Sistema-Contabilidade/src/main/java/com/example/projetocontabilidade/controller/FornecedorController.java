package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.model.Fornecedor;
import com.example.projetocontabilidade.records.ClienteDTO.GetClienteDTO;
import com.example.projetocontabilidade.records.ClienteDTO.UpdateClienteDTO;
import com.example.projetocontabilidade.records.FornecedorDTO.CreateFornecedorDTO;
import com.example.projetocontabilidade.records.FornecedorDTO.GetFornecedorDTO;
import com.example.projetocontabilidade.records.FornecedorDTO.UpdateFornecedorDTO;
import com.example.projetocontabilidade.service.FornecedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listFornecedores(Model model) {
        model.addAttribute("title", "Lista de Fornecedores");
        model.addAttribute("fornecedores", fornecedorService.getAllFornecedores());
        return "fornecedores/list";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        model.addAttribute("title", "Novo Fornecedor");
        model.addAttribute("fornecedor", new Fornecedor());
        return "fornecedores/insert";
    }

    @PostMapping("/salvar")
    public String saveFornecedor(@ModelAttribute CreateFornecedorDTO fornecedor) {
        fornecedorService.save(fornecedor);
        return "redirect:/fornecedores";
    }

    @GetMapping("/editar/{id}")
    public String showFormEdit(@PathVariable Long id, Model model) {
        GetFornecedorDTO fornecedor = fornecedorService.getByIdFornecedor(id).orElseThrow(() -> new RuntimeException("fornecedor não encontrado"));
        model.addAttribute("fornecedor", fornecedor);
        return "fornecedores/edit";
    }

    @PutMapping("/editar/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UpdateFornecedorDTO fornecedorDTO) {
        fornecedorService.update(id, fornecedorDTO);
        return "redirect:/fornecedores";
    }
}