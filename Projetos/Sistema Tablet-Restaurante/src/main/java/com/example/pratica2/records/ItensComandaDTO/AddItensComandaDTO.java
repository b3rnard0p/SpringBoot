package com.example.pratica2.records.ItensComandaDTO;

import java.util.List;

public record AddItensComandaDTO(
        List<CreateItensComandaDTO> itens
)  {
}
