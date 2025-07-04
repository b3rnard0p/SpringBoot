package com.example.projetocontabilidade.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.projetocontabilidade.records.DespesasDTO.CreateDespesasDTO;
import com.example.projetocontabilidade.records.DespesasDTO.GetDespesasDTO;
import com.example.projetocontabilidade.service.DespesasService;

@Controller
@RequestMapping("/despesa")
public class DespesaController {

    private final DespesasService despesasService;

    public DespesaController(DespesasService despesasService) {
        this.despesasService = despesasService;
    }

    // Exibe formulário para nova despesa
    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("createDespesasDTO", new CreateDespesasDTO(null,null, null));
        return "Despesas/Form";
    }

    // Processa o envio do formulário
    @PostMapping("/salvar")
    public String saveDespesa(@ModelAttribute("createDespesasDTO") CreateDespesasDTO dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "Despesas/Form";
        }
        despesasService.save(dto);
        return "redirect:/despesa/list";
    }

    // Lista todas as despesas
    @GetMapping("/list")
    public String listDespesas(Model model) {
        List<GetDespesasDTO> lista = despesasService.getAllDespesas();
        model.addAttribute("despesas", lista);
        return "Despesas/List";
    }

    // Lista despesas em aberto
    @GetMapping("/abertas")
    public String listDespesasEmAberto(Model model) {
        List<GetDespesasDTO> lista = despesasService.getDespesasEmAberto();
        model.addAttribute("despesas", lista);
        return "Despesas/List";
    }

    // Pagar uma despesa
    @PostMapping("/pagar/{id}")
    public String pagarDespesa(@PathVariable Long id) {
        despesasService.pagarDespesa(id);
        return "redirect:/despesa/list";
    }

}
