package com.example.projetocontabilidade.controller;

import com.example.projetocontabilidade.model.ParcelaCompra;
import com.example.projetocontabilidade.records.CompraDTO.CreateCompraDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;
import com.example.projetocontabilidade.repository.ParcelaCompraRepository;
import com.example.projetocontabilidade.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final ParcelaCompraRepository parcelaRepository;
    private final CaixaService caixaService;
    private final APagarCompraService aPagarService;

    public CompraController(CompraService compraService, FornecedorService fornecedorService, ProdutoService produtoService,
                            ParcelaCompraRepository parcelaRepository, CaixaService caixaService, APagarCompraService aPagarService) {
        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.parcelaRepository = parcelaRepository;
        this.caixaService = caixaService;
        this.aPagarService = aPagarService;
    }

    @GetMapping
    public String listCompras(Model model) {
        model.addAttribute("title", "Lista de Compras");  // Corrigido o título
        model.addAttribute("produtos", compraService.getAllCompras());
        return "compras/list";
    }

    @GetMapping("/novo")
    public String showForm(Model model) {
        CreateProdutoDTO produtoDTO = new CreateProdutoDTO("", BigDecimal.ZERO, BigDecimal.ZERO, 0);
        CreateCompraDTO compraDTO = new CreateCompraDTO(null, null, null, null, produtoDTO, null,
                false, null, null, null, null);
        model.addAttribute("produtos", produtoService.getAllProdutos());
        model.addAttribute("compraDTO", compraDTO);
        model.addAttribute("fornecedores", fornecedorService.getAllFornecedores());
        model.addAttribute("title", "Nova Compra");
        return "compras/insert";
    }


    @PostMapping("/salvar")
    public String saveCompra(@ModelAttribute CreateCompraDTO compraDTO) {
        compraService.save(compraDTO);
        return "redirect:/compras";
    }

    @PostMapping("/{compraId}/parcelas/{parcelaId}/pagar")
    public String receberParcela(
            @PathVariable Long compraId,
            @PathVariable Long parcelaId
    ) {

        ParcelaCompra parcela = parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new IllegalArgumentException("Parcela não encontrada"));

        if (!parcela.getCompra().getId().equals(compraId)) {
            throw new IllegalArgumentException("Parcela não pertence à venda");
        }

        if (parcela.isPaga()) {
            throw new IllegalStateException("Parcela já está paga");
        }

        aPagarService.pagarParcela(compraId);

        parcela.setPaga(true);
        parcelaRepository.save(parcela);

        return "redirect:/compras/contas-a-pagar";
    }

    @GetMapping("/contas-a-pagar")
    public String showContasAReceber(Model model) {
        List<ParcelaCompra> parcelasPendentes = parcelaRepository.findByPagaFalseOrderByDataVencimento();
        model.addAttribute("parcelasPendentes", parcelasPendentes);
        return "compras/contas-a-pagar";
    }
}