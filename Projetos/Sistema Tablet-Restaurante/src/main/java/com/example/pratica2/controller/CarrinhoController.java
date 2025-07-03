package com.example.pratica2.controller;

import com.example.pratica2.models.Tipo;
import com.example.pratica2.records.ComandaDTO.ComandaResponseDTO;
import com.example.pratica2.service.CarrinhoService;
import com.example.pratica2.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {
    private final CarrinhoService carrinhoService;
    private final UsuarioService usuarioService;

    public CarrinhoController(CarrinhoService carrinhoService,
                              UsuarioService usuarioService) {
        this.carrinhoService = carrinhoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String verCarrinho(Model model) {
        model.addAttribute("itens", carrinhoService.getItensCarrinho());
        model.addAttribute("total", carrinhoService.calcularTotal());
        model.addAttribute("observacoes", carrinhoService.getObservacoes());
        return "Cliente/Carrinho";
    }

    @PostMapping("/adicionar/{produtoId}")
    public String adicionarProduto(@PathVariable Long produtoId,
                                   @RequestHeader(value = "referer", required = false) String referer,
                                   RedirectAttributes redirectAttributes) {
        carrinhoService.adicionarProduto(produtoId);
        redirectAttributes.addFlashAttribute("successMessage", "Item adicionado ao carrinho!");
        return "redirect:" + (referer != null ? referer : "/produto/list");
    }

    @PostMapping("/atualizar")
    public String atualizarQuantidades(@RequestParam Map<String, String> quantidades) {
        carrinhoService.atualizarQuantidades(quantidades);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover/{produtoId}")
    public String removerProduto(@PathVariable Long produtoId,
                                 RedirectAttributes redirectAttributes) {
        carrinhoService.removerProduto(produtoId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removido com sucesso!");
        return "redirect:/carrinho";
    }

    @PostMapping("/finalizar")
    public String finalizarPedido(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Tipo tipoMesa = usuarioService.obterTipoMesaAtual(session);
            ComandaResponseDTO comandaResponse = carrinhoService.finalizarPedido(session);

            return "redirect:/comanda/detalhes/" + tipoMesa.name();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrinho";
        }
    }

    @PostMapping("/salvar-observacoes")
    public String salvarObservacoes(@RequestParam String observacoes,
                                    RedirectAttributes redirectAttributes) {
        carrinhoService.salvarObservacoes(observacoes);
        redirectAttributes.addFlashAttribute("successMessage", "Observações salvas com sucesso!");
        return "redirect:/carrinho";
    }
}
