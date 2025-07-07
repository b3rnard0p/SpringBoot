package com.example.projetocontabilidade.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.projetocontabilidade.model.Despesas;
import com.example.projetocontabilidade.records.DespesasDTO.CreateDespesasDTO;
import com.example.projetocontabilidade.records.DespesasDTO.GetDespesasDTO;
import com.example.projetocontabilidade.repository.DespesasRepository;

@Service
public class DespesasService {

    private final DespesasRepository despesasRepository;
    private final CaixaService caixaService;

    public DespesasService(DespesasRepository despesasRepository, CaixaService caixaService){
        this.despesasRepository = despesasRepository;
        this.caixaService = caixaService;
    }

    public Despesas save(CreateDespesasDTO dto) {
        Despesas despesa = new Despesas();
        despesa.setNome(dto.nome());
        despesa.setValor(dto.valor());
        return despesasRepository.save(despesa);
    }

    public List<GetDespesasDTO> getAllDespesas() {
        return despesasRepository.findAll().stream()
                .map(despesa -> new GetDespesasDTO(
                        despesa.getId(),
                        despesa.getNome(),
                        despesa.getValor(),
                        despesa.isPago()
                ))
                .toList();
    }

    public Optional<GetDespesasDTO> getByIdDespesa(Long id) {
        return despesasRepository.findById(id)
                .map(despesa -> new GetDespesasDTO(
                        despesa.getId(),
                        despesa.getNome(),
                        despesa.getValor(),
                        despesa.isPago()
                ));
    }

    public BigDecimal getTotalDespesas() {
        return despesasRepository.findByPagoFalse().stream()
                .map(Despesas::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<GetDespesasDTO> getDespesasEmAberto() {
        return despesasRepository.findByPagoFalse().stream()
                .map(despesa -> new GetDespesasDTO(
                        despesa.getId(),
                        despesa.getNome(),
                        despesa.getValor(),
                        despesa.isPago()
                ))
                .toList();
    }

    public boolean pagarDespesa(Long id) {
        Optional<Despesas> optional = despesasRepository.findById(id);
        if (optional.isPresent()) {
            Despesas despesa = optional.get();
            if (!despesa.isPago()) {
                despesa.setPago(true);
                despesasRepository.save(despesa);
                caixaService.subtrairDoCaixa(despesa.getValor());
            }
            return true;
        }
        return false;
    }
}
