package com.example.pratica2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pratica2.models.Tipo;
import com.example.pratica2.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("tiposUsuario", Tipo.values());
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("tipoUsuario") String tipoUsuario,
                              Model model,
                              HttpSession session,
                              UsuarioService usuarioService) {

        if (tipoUsuario != null) {
            tipoUsuario = tipoUsuario.trim().toUpperCase();

            if (tipoUsuario.matches("MESA[1-3]")) {
                String numeroMesa = tipoUsuario.substring(4);
                tipoUsuario = "Mesa " + numeroMesa;
            }
        }

        return usuarioService.processarLogin(tipoUsuario, session);
    }
}
