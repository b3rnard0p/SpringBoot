package com.example.pratica2.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import com.example.pratica2.models.Tipo;

@Service
public class UsuarioService {

    public String processarLogin(String tipoUsuario, HttpSession session) {
        if ("ADMIN".equalsIgnoreCase(tipoUsuario)) {
            session.setAttribute("tipoUsuario", Tipo.ADMIN.name());
            session.removeAttribute("mesa");
            return "redirect:/produto/list";
        }
        else if (tipoUsuario.matches("(?i)Mesa [1-3]")) {
            session.setAttribute("mesa", tipoUsuario);
            session.removeAttribute("tipoUsuario");
            return "redirect:/cliente/list";
        }

        throw new IllegalArgumentException("Tipo de usuário inválido");
    }

    public Tipo obterTipoMesaAtual(HttpSession session) {
        String mesa = (String) session.getAttribute("mesa");
        if (mesa == null) throw new IllegalStateException("Mesa não autenticada");

        switch(mesa.toUpperCase()) {
            case "MESA 1": return Tipo.MESA1;
            case "MESA 2": return Tipo.MESA2;
            case "MESA 3": return Tipo.MESA3;
            default: throw new IllegalStateException("Mesa inválida");
        }
    }
}