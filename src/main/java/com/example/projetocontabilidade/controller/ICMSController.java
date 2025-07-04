package com.example.projetocontabilidade.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projetocontabilidade.service.ICMSService;

@Controller
public class ICMSController {
    private final ICMSService icmsService;

    public ICMSController(ICMSService icmsService) {
        this.icmsService = icmsService;
    }

    @PostMapping("/icms/pagar")
    public String pagarDebitoICMS(@RequestParam("valor") BigDecimal valor, Model model) {
        icmsService.pagarDebitoICMS(valor);
        return "redirect:/";
    }
} 