package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Cliente;
import com.example.projetocontabilidade.records.ClienteDTO.CreateClienteDTO;
import com.example.projetocontabilidade.records.ClienteDTO.GetClienteDTO;
import com.example.projetocontabilidade.records.ClienteDTO.UpdateClienteDTO;
import com.example.projetocontabilidade.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente save(CreateClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setCidade(clienteDTO.cidade());
        cliente.setEstado(clienteDTO.estado());
        cliente.setCpf(clienteDTO.cpf());
        return clienteRepository.save(cliente);
    }

    public List<GetClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(cliente -> new GetClienteDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getCidade(),
                        cliente.getEstado()
                ))
                .toList();
    }

    public Optional<GetClienteDTO> getByIdCliente(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> new GetClienteDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getCidade(),
                        cliente.getEstado()
                ));
    }

    public UpdateClienteDTO update(Long id, UpdateClienteDTO clienteDTO) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteDTO.nome());
                    cliente.setCidade(clienteDTO.cidade());
                    cliente.setEstado(clienteDTO.estado());
                    cliente.setCpf(clienteDTO.cpf());
                    Cliente updatedCliente = clienteRepository.save(cliente);
                    return new UpdateClienteDTO(
                            updatedCliente.getNome(),
                            updatedCliente.getCidade(),
                            updatedCliente.getEstado(),
                            updatedCliente.getCpf()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));
    }
}