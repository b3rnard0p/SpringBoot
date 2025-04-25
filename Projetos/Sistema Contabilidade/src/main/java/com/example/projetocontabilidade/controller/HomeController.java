package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.service.CaixaService;
import com.example.projetocontabilidade.service.ICMSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class HomeController {

    private final ICMSService icmsService;
    private final CaixaService caixaService;

    public HomeController(ICMSService icmsService, CaixaService caixaService) {
        this.icmsService = icmsService;
        this.caixaService = caixaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        BigDecimal saldoICMS = icmsService.getSaldoCredito();
        BigDecimal debitoICMS = icmsService.getSaldoDebito();
        model.addAttribute("saldoICMS", saldoICMS != null ? saldoICMS : BigDecimal.ZERO);
        model.addAttribute("debitoICMS", debitoICMS != null ? debitoICMS : BigDecimal.ZERO);
        model.addAttribute("saldoCaixa", caixaService.getSaldo());
        return "Home";
    }
}