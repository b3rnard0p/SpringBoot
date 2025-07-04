package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.model.Empresa;
import com.example.projetocontabilidade.repository.EmpresaRepository;
import com.example.projetocontabilidade.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class HomeController {

    private final ICMSService icmsService;
    private final CaixaService caixaService;
    private final PatrimonioService patrimonioService;
    private final EmpresaRepository empresaRepository;
    private final ParcelaService parcelaService;
    private final DespesasService despesasService;

    public HomeController(ICMSService icmsService, CaixaService caixaService,
                          PatrimonioService patrimonioService, EmpresaRepository empresaRepository,
                          ParcelaService parcelaService, DespesasService despesasService) {
        this.icmsService = icmsService;
        this.caixaService = caixaService;
        this.patrimonioService = patrimonioService;
        this.empresaRepository = empresaRepository;
        this.parcelaService = parcelaService;
        this.despesasService = despesasService;
    }

    @GetMapping("/")
    public String home(Model model) {
        boolean empresaCadastrada = empresaRepository.count() > 0;
        model.addAttribute("empresaCadastrada", empresaCadastrada);

        if (empresaCadastrada) {
            Empresa empresa = empresaRepository.findAll().get(0);

            BigDecimal saldoICMS = Optional.ofNullable(icmsService.getSaldoCredito()).orElse(BigDecimal.ZERO);
            BigDecimal debitoICMS = Optional.ofNullable(icmsService.getSaldoDebito()).orElse(BigDecimal.ZERO);

            model.addAttribute("saldoICMS", saldoICMS);
            model.addAttribute("debitoICMS", debitoICMS);
            model.addAttribute("saldoCaixa", caixaService.getSaldo());
            model.addAttribute("saldoPatrimonio", patrimonioService.getSaldo());
            model.addAttribute("capitalSocial", empresa.getCapitalSocial());
            model.addAttribute("empresa", empresa);

            // Adiciona os totais de parcelas
            model.addAttribute("totalParcelasAReceber", parcelaService.getTotalParcelasAReceber());
            model.addAttribute("totalParcelasAPagar", parcelaService.getTotalParcelasAPagar());

            model.addAttribute("totalDespesas", despesasService.getTotalDespesas());
        } else {
            model.addAttribute("empresa", new Empresa());
        }

        return "Home";
    }


    @PostMapping("/cadastrar-empresa")
    public String cadastrarEmpresa(@ModelAttribute Empresa empresa) {
        empresaRepository.save(empresa);
        return "redirect:/";
    }
}