package com.example.projetocontabilidade.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.projetocontabilidade.service.BalancoPatrimonialService;
import com.example.projetocontabilidade.service.PDFService;

@Controller
public class BalancoController {

    private static final Logger logger = LoggerFactory.getLogger(BalancoController.class);
    
    private final BalancoPatrimonialService balancoService;
    private final PDFService pdfService;

    public BalancoController(BalancoPatrimonialService balancoService, PDFService pdfService) {
        this.balancoService = balancoService;
        this.pdfService = pdfService;
        logger.info("BalancoController inicializado com sucesso");
    }

    @GetMapping("/relatorios/balanco-patrimonial")
    public String balancoPatrimonial(Model model) {
        logger.info("Acessando página do balanço patrimonial");
        Map<String, Map<String, BigDecimal>> balanco = balancoService.gerarBalancoPatrimonial();

        BigDecimal totalAtivo = balanco.get("ATIVO").values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPassivo = balanco.get("PASSIVO").values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal capitalSocial = balanco.get("PATRIMÔNIO LÍQUIDO").get("Capital Social");

        // NOVO cálculo: Resultado = ATIVO - PASSIVO
        BigDecimal resultadoExercicio = totalAtivo.subtract(totalPassivo);
        // Atualiza no mapa usando exatamente esta chave
        balanco.get("PATRIMÔNIO LÍQUIDO")
                .put("Resultado do Exercício", resultadoExercicio);

        // Patrimônio Líquido Total = Capital Social + Resultado
        BigDecimal totalPatrimonio = capitalSocial.add(resultadoExercicio);

        model.addAttribute("balanco", balanco);
        model.addAttribute("totalAtivo", totalAtivo);
        model.addAttribute("totalPassivo", totalPassivo);
        model.addAttribute("totalPatrimonio", totalPatrimonio);
        model.addAttribute("title", "Balanço Patrimonial");

        return "balanco-patrimonial";
    }

    @GetMapping("/api/balanco-patrimonial/pdf")
    public ResponseEntity<ByteArrayResource> exportarPDF() {
        logger.info("Iniciando exportação do PDF do balanço patrimonial");
        try {
            Map<String, Map<String, BigDecimal>> balanco = balancoService.gerarBalancoPatrimonial();

            BigDecimal totalAtivo = balanco.get("ATIVO").values().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalPassivo = balanco.get("PASSIVO").values().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal capitalSocial = balanco.get("PATRIMÔNIO LÍQUIDO").get("Capital Social");

            // Cálculo: Resultado = ATIVO - PASSIVO
            BigDecimal resultadoExercicio = totalAtivo.subtract(totalPassivo);
            balanco.get("PATRIMÔNIO LÍQUIDO")
                    .put("Resultado do Exercício", resultadoExercicio);

            // Patrimônio Líquido Total = Capital Social + Resultado
            BigDecimal totalPatrimonio = capitalSocial.add(resultadoExercicio);

            logger.info("Gerando PDF...");
            byte[] pdfBytes = pdfService.gerarPDFBalancoPatrimonial(balanco, totalAtivo, totalPassivo, totalPatrimonio);

            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String filename = "balanco-patrimonial-" + dataAtual + ".pdf";

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            logger.info("PDF gerado com sucesso. Tamanho: {} bytes", pdfBytes.length);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (Exception e) {
            logger.error("Erro ao gerar PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/teste-pdf")
    public ResponseEntity<String> testePDF() {
        logger.info("Endpoint de teste acessado");
        return ResponseEntity.ok("Controller funcionando!");
    }
}