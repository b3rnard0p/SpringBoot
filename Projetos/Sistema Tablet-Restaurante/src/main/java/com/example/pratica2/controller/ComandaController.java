package com.example.pratica2.controller;

import com.example.pratica2.models.Tipo;
import com.example.pratica2.records.ComandaDTO.CloseComandaDTO;
import com.example.pratica2.records.ComandaDTO.ComandaResponseDTO;
import com.example.pratica2.service.ComandaService;
import com.example.pratica2.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/comanda")
public class ComandaController {

    @Autowired
    private ComandaService comandaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/detalhes/{mesa}")
    public String detalhesComandaPorMesa(
            @PathVariable String mesa,
            HttpSession session,
            Model model) {
        Tipo tipoMesa = Tipo.valueOf(mesa.toUpperCase());
        Optional<ComandaResponseDTO> comandaOptional = comandaService.buscarComandaAtivaPorMesa(tipoMesa);

        ComandaResponseDTO comandaDTO = comandaOptional.orElseGet(() ->
                new ComandaResponseDTO(null, tipoMesa, "ABERTA", LocalDateTime.now(), 0.0, Collections.emptyList()
                )
        );
        model.addAttribute("comanda", comandaDTO);
        return "Cliente/Comanda";
    }

    @PostMapping("/fechar/{id}")
    public String fecharComanda(@PathVariable Long id) {
        CloseComandaDTO fecharDTO = new CloseComandaDTO(id);
        ComandaResponseDTO comandaDTO = comandaService.fecharComanda(fecharDTO);

        return "redirect:/comanda/detalhes/" + comandaDTO.mesa().name().toLowerCase()
                + "?success=Comanda fechada com sucesso";
    }
}