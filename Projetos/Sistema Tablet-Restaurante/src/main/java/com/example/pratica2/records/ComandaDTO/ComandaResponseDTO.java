package com.example.pratica2.records.ComandaDTO;

import com.example.pratica2.models.Tipo;
import com.example.pratica2.records.ItensComandaDTO.CreateItensComandaDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ComandaResponseDTO(
        Long id,
        Tipo mesa,
        String status,
        LocalDateTime dataAbertura,
        Double valorTotal,
        List<CreateItensComandaDTO> itens
) {}