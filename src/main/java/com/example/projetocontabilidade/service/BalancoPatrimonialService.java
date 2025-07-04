package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.*;
import com.example.projetocontabilidade.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class BalancoPatrimonialService {

    private final BemRepository bemRepository;
    private final CaixaService caixaService;
    private final ICMSService icmsService;
    private final ParcelaService parcelaService;
    private final EmpresaRepository empresaRepository;
    private final DespesasService despesasService;


    public BalancoPatrimonialService(BemRepository bemRepository,
                                     CaixaService caixaService,
                                     ICMSService icmsService,
                                     ParcelaService parcelaService,
                                     EmpresaRepository empresaRepository, DespesasService despesasService) {
        this.bemRepository = bemRepository;
        this.caixaService = caixaService;
        this.icmsService = icmsService;
        this.parcelaService = parcelaService;
        this.empresaRepository = empresaRepository;
        this.despesasService = despesasService;
    }

    public Map<String, Map<String, BigDecimal>> gerarBalancoPatrimonial() {
        Map<String, Map<String, BigDecimal>> balanco = new HashMap<>();

        Map<String, BigDecimal> ativo = new HashMap<>();
        ativo.put("Caixa", caixaService.getSaldo());
        ativo.put("Contas a Receber", parcelaService.getTotalParcelasAReceber());
        ativo.put("ICMS a Recuperar", icmsService.getSaldoCredito());
        BigDecimal totalBensPatrimoniais = bemRepository.findAll().stream()
                .map(bem -> bem.getPreco()
                        .multiply(BigDecimal.valueOf(bem.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ativo.put("Bens Patrimoniais", totalBensPatrimoniais);
        balanco.put("ATIVO", ativo);

        Map<String, BigDecimal> passivo = new HashMap<>();
        passivo.put("Contas a Pagar", parcelaService.getTotalParcelasAPagar());
        passivo.put("Despesas", despesasService.getTotalDespesas());
        passivo.put("ICMS a Recolher", icmsService.getSaldoDebito());
        balanco.put("PASSIVO", passivo);

        Map<String, BigDecimal> pl = new HashMap<>();
        BigDecimal capitalSocial = empresaRepository.findById(1L)
                .map(Empresa::getCapitalSocial)
                .orElse(BigDecimal.ZERO);
        pl.put("Capital Social", capitalSocial);
        balanco.put("PATRIMÔNIO LÍQUIDO", pl);

        return balanco;
    }

}