package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.model.Cliente;
import com.example.projetocontabilidade.records.ClienteDTO.CreateClienteDTO;
import com.example.projetocontabilidade.records.ClienteDTO.GetClienteDTO;
import com.example.projetocontabilidade.records.ClienteDTO.UpdateClienteDTO;
import com.example.projetocontabilidade.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("title", "Lista de Clientes");
        model.addAttribute("clientes", clienteService.getAllClientes());
        return "clientes/list";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        model.addAttribute("title", "Novo Cliente");
        model.addAttribute("cliente", new Cliente());
        return "clientes/insert";
    }

    @PostMapping("/salvar")
    public String saveCliente(@ModelAttribute CreateClienteDTO cliente) {
        clienteService.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String showFormEdit(@PathVariable Long id, Model model) {
        GetClienteDTO cliente = clienteService.getByIdCliente(id).orElseThrow(() -> new RuntimeException("cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/edit";
    }

    @PutMapping("/editar/{id}")
    public String update(@PathVariable Long id, @ModelAttribute UpdateClienteDTO clienteDTO) {
        clienteService.update(id, clienteDTO);
        return "redirect:/clientes";
    }
}