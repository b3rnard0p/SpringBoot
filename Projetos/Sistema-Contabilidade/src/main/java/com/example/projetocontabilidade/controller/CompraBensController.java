package com.example.projetocontabilidade.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.projetocontabilidade.model.ParcelaBens;
import com.example.projetocontabilidade.records.BemDTO.CreateBemDTO;
import com.example.projetocontabilidade.records.BemDTO.GetBemDTO;
import com.example.projetocontabilidade.records.CompraBensDTO.CreateCompraBensDTO;
import com.example.projetocontabilidade.records.CompraBensDTO.GetCompraBensDTO;
import com.example.projetocontabilidade.repository.ParcelaBensRepository;
import com.example.projetocontabilidade.service.APagarBensService;
import com.example.projetocontabilidade.service.BemService;
import com.example.projetocontabilidade.service.CaixaService;
import com.example.projetocontabilidade.service.CompraBensService;

@Controller
@RequestMapping("/compra-bens")
public class CompraBensController {

    private final CompraBensService compraBensService;
    private final BemService bemService;
    private final ParcelaBensRepository parcelaRepository;
    private final CaixaService caixaService;
    private final APagarBensService aPagarBensService;

    public CompraBensController(CompraBensService compraBensService, BemService bemService,
                                ParcelaBensRepository parcelaRepository, CaixaService caixaService,
                                APagarBensService aPagarBensService) {
        this.compraBensService = compraBensService;
        this.bemService = bemService;
        this.parcelaRepository = parcelaRepository;
        this.caixaService = caixaService;
        this.aPagarBensService = aPagarBensService;
    }

    @GetMapping("/list")
    public String listarCompras(Model model) {
        List<GetCompraBensDTO> compras = compraBensService.getAllCompras();
        model.addAttribute("compras", compras);
        return "CompraBens/List";
    }

    @GetMapping("/form")
    public String mostrarFormulario(Model model) {
        List<GetBemDTO> bens = bemService.getAllBens();
        // inicializa 'bem' para evitar null-pointer no Thymeleaf
        CreateBemDTO bemDTO = new CreateBemDTO(
                "",
                null,
                null
        );
        CreateCompraBensDTO compraDTO = new CreateCompraBensDTO(
                true,
                LocalDate.now(),
                null,
                null,
                bemDTO,
                null,
                null,
                null,
                null
        );

        model.addAttribute("bens", bens);
        model.addAttribute("compra", compraDTO);
        return "CompraBens/Form";
    }


    @PostMapping("/save")
    public String salvarCompra(@ModelAttribute CreateCompraBensDTO compraDTO) {
        compraBensService.save(compraDTO);
        return "redirect:/compra-bens/list";
    }

    @PostMapping("/{compraBensId}/parcelas/{parcelaId}/pagar")
    public String receberParcela(
            @PathVariable Long compraBensId,
            @PathVariable Long parcelaId
    ) {
        ParcelaBens parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada"));

        if (!parcela.getCompraBens().getId().equals(compraBensId)) {
            throw new IllegalArgumentException("Parcela não pertence à venda");
        }

        if (parcela.isPaga()) {
            throw new IllegalStateException("Parcela já está paga");
        }

        aPagarBensService.pagarParcela(compraBensId);

        // Marca parcela como paga
        parcela.setPaga(true);
        parcelaRepository.save(parcela);

        return "redirect:/compra-bens/contas-a-pagar-bens";
    }

    @GetMapping("/contas-a-pagar-bens")
    public String showContasAReceber(Model model) {
        List<ParcelaBens> parcelasPendentes = parcelaRepository.findByPagaFalseOrderByDataVencimento();
        model.addAttribute("parcelasPendentes", parcelasPendentes);
        return "CompraBens/contas-a-pagar-bens";
    }
}