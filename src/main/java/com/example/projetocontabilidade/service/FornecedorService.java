package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Fornecedor;
import com.example.projetocontabilidade.records.FornecedorDTO.CreateFornecedorDTO;
import com.example.projetocontabilidade.records.FornecedorDTO.GetFornecedorDTO;
import com.example.projetocontabilidade.records.FornecedorDTO.UpdateFornecedorDTO;
import com.example.projetocontabilidade.repository.FornecedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public Fornecedor save(CreateFornecedorDTO fornecedorDTO) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(fornecedorDTO.nome());
        fornecedor.setCnpj(fornecedorDTO.cnpj());
        fornecedor.setCidade(fornecedorDTO.cidade());
        fornecedor.setEstado(fornecedorDTO.estado());
        return fornecedorRepository.save(fornecedor);
    }

    public List<GetFornecedorDTO> getAllFornecedores() {
        return fornecedorRepository.findAll().stream()
                .map(fornecedor -> new GetFornecedorDTO(
                        fornecedor.getId(),
                        fornecedor.getNome(),
                        fornecedor.getCidade(),
                        fornecedor.getEstado(),
                        fornecedor.getCnpj()
                ))
                .toList();
    }

    public Optional<GetFornecedorDTO> getByIdFornecedor(Long id) {
        return fornecedorRepository.findById(id)
                .map(fornecedor -> new GetFornecedorDTO(
                        fornecedor.getId(),
                        fornecedor.getNome(),
                        fornecedor.getCidade(),
                        fornecedor.getEstado(),
                        fornecedor.getCnpj()
                ));
    }

    public UpdateFornecedorDTO update(Long id, UpdateFornecedorDTO fornecedorDTO) {
        return fornecedorRepository.findById(id)
                .map(fornecedor -> {
                    fornecedor.setNome(fornecedorDTO.nome());
                    fornecedor.setCnpj(fornecedorDTO.cnpj());
                    fornecedor.setCidade(fornecedorDTO.cidade());
                    fornecedor.setEstado(fornecedorDTO.estado());

                    Fornecedor updatedfornecedor = fornecedorRepository.save(fornecedor);

                    return new UpdateFornecedorDTO(
                            updatedfornecedor.getNome(),
                            updatedfornecedor.getCnpj(),
                            updatedfornecedor.getCidade(),
                            updatedfornecedor.getEstado()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "fornecedor não encontrado"
                ));
    }
}
