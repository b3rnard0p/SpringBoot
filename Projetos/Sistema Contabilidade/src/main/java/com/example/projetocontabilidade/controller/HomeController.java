package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.service.ICMSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class HomeController {

    private final ICMSService icmsService;

    public HomeController(ICMSService icmsService) {
        this.icmsService = icmsService;
    }

    @GetMapping("/")
    public String home(Model model) {
        BigDecimal saldoICMS = icmsService.getSaldoCredito();
        BigDecimal debitoICMS = icmsService.getSaldoDebito();
        model.addAttribute("saldoICMS", saldoICMS != null ? saldoICMS : BigDecimal.ZERO);
        model.addAttribute("debitoICMS", debitoICMS != null ? debitoICMS : BigDecimal.ZERO);
        return "Home";
    }
}