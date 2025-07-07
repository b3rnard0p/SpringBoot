package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Bem;
import com.example.projetocontabilidade.records.BemDTO.CreateBemDTO;
import com.example.projetocontabilidade.records.BemDTO.GetBemDTO;
import com.example.projetocontabilidade.records.BemDTO.UpdateBemDTO;
import com.example.projetocontabilidade.repository.BemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BemService {

    private final BemRepository bemRepository;

    public BemService(BemRepository bemRepository) {
        this.bemRepository = bemRepository;
    }

    public Bem save(CreateBemDTO bemDTO) {
        Bem bem= new Bem();
        bem.setNome(bemDTO.nome());
        bem.setPreco(bemDTO.preco());
        bem.setQuantidade(bemDTO.quantidade());
        return bemRepository.save(bem);
    }

    public List<GetBemDTO> getAllBens() {
        return bemRepository.findAll().stream()
                .map(bem -> new GetBemDTO(
                        bem.getId(),
                        bem.getNome(),
                        bem.getPreco(),
                        bem.getQuantidade()
                ))
                .toList();
    }

    public Bem getBemEntityById(Long id) {
        return bemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bem não encontrado"));
    }

    public Optional<GetBemDTO> getByIdBem(Long id) {
        return bemRepository.findById(id)
                .map(bem -> new GetBemDTO(
                        bem.getId(),
                        bem.getNome(),
                        bem.getPreco(),
                        bem.getQuantidade()
                ));
    }
    public UpdateBemDTO update(Long id, UpdateBemDTO bemDTO) {
        return bemRepository.findById(id)
                .map(bem -> {
                    bem.setNome(bemDTO.nome());
                    bem.setPreco(bemDTO.preco());


                    if (bemDTO.quantidade() != null) {
                        bem.setQuantidade(bemDTO.quantidade());
                    }

                    Bem updatedbem = bemRepository.save(bem);

                    return new UpdateBemDTO(
                            updatedbem.getNome(),
                            updatedbem.getPreco(),
                            updatedbem.getQuantidade()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));
    }
}
