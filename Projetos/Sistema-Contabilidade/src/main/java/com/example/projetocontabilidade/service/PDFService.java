package com.example.projetocontabilidade.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class PDFService {

    public byte[] gerarPDFBalancoPatrimonial(Map<String, Map<String, BigDecimal>> balanco, 
                                           BigDecimal totalAtivo, 
                                           BigDecimal totalPassivo, 
                                           BigDecimal totalPatrimonio) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            String html = gerarHTMLBalanco(balanco, totalAtivo, totalPassivo, totalPatrimonio);
            
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri("data:text/html;charset=utf-8," + html);
            builder.toStream(baos);
            builder.run();
            
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private String gerarHTMLBalanco(Map<String, Map<String, BigDecimal>> balanco, 
                                  BigDecimal totalAtivo, 
                                  BigDecimal totalPassivo, 
                                  BigDecimal totalPatrimonio) {
        
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>");
        html.append("<meta charset=\"UTF-8\"></meta>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".titulo { text-align: center; font-size: 22px; font-weight: bold; margin-bottom: 20px; }");
        html.append(".data { text-align: center; font-size: 12px; margin-bottom: 30px; }");
        html.append("table { width: 80%; margin: 0 auto; border-collapse: separate; border-spacing: 20px 0; }");
        html.append("th, td { vertical-align: top; }");
        html.append(".coluna { width: 320px; min-width: 220px; }");
        html.append(".header { background-color: #22c55e; color: white; padding: 10px; text-align: center; font-weight: bold; font-size: 16px; }");
        html.append(".header-passivo { background-color: #ef4444; }");
        html.append(".header-pl { background-color: #9333ea; }");
        html.append(".conteudo { padding: 10px; border: 1px solid #ddd; background: #fff; }");
        html.append(".linha { display: flex; justify-content: space-between; padding: 4px 0; border-bottom: 1px solid #eee; font-size: 14px; }");
        html.append(".linha:last-child { border-bottom: none; }");
        html.append(".total { font-weight: bold; background-color: #f3f4f6; }");
        html.append(".valor { text-align: right; min-width: 80px; display: inline-block; }");
        html.append(".observacao { text-align: center; font-size: 10px; margin-top: 30px; color: #666; }");
        html.append("</style></head><body>");
        
        html.append("<div class=\"titulo\">BALANÇO PATRIMONIAL</div>");
        html.append("<div class=\"data\">Data: ").append(dataAtual).append("</div>");
        
        html.append("<table>");
        // Primeira linha: ATIVO | PASSIVO
        html.append("<tr>");
        // ATIVO
        html.append("<td class=\"coluna\">");
        html.append("<div class=\"header\">ATIVO</div>");
        html.append("<div class=\"conteudo\">");
        for (Map.Entry<String, BigDecimal> entry : balanco.get("ATIVO").entrySet()) {
            html.append("<div class=\"linha\"><span>").append(entry.getKey()).append("</span>")
                .append("<span class=\"valor\">R$ ").append(formatarValor(entry.getValue())).append("</span></div>");
        }
        html.append("<div class=\"linha total\"><span>TOTAL</span><span class=\"valor\">R$ ").append(formatarValor(totalAtivo)).append("</span></div>");
        html.append("</div></td>");
        // PASSIVO
        html.append("<td class=\"coluna\">");
        html.append("<div class=\"header header-passivo\">PASSIVO</div>");
        html.append("<div class=\"conteudo\">");
        for (Map.Entry<String, BigDecimal> entry : balanco.get("PASSIVO").entrySet()) {
            html.append("<div class=\"linha\"><span>").append(entry.getKey()).append("</span>")
                .append("<span class=\"valor\">R$ ").append(formatarValor(entry.getValue())).append("</span></div>");
        }
        html.append("<div class=\"linha total\"><span>TOTAL</span><span class=\"valor\">R$ ").append(formatarValor(totalPassivo)).append("</span></div>");
        html.append("</div></td>");
        html.append("</tr>");
        // Segunda linha: PATRIMÔNIO LÍQUIDO centralizado
        html.append("<tr>");
        html.append("<td colspan=\"2\" style=\"text-align:center;\">");
        html.append("<div style=\"display:inline-block; min-width:320px;\">");
        html.append("<div class=\"header header-pl\">PATRIMÔNIO LÍQUIDO</div>");
        html.append("<div class=\"conteudo\">");
        for (Map.Entry<String, BigDecimal> entry : balanco.get("PATRIMÔNIO LÍQUIDO").entrySet()) {
            html.append("<div class=\"linha\"><span>").append(entry.getKey()).append("</span>")
                .append("<span class=\"valor\">R$ ").append(formatarValor(entry.getValue())).append("</span></div>");
        }
        html.append("<div class=\"linha total\"><span>TOTAL</span><span class=\"valor\">R$ ").append(formatarValor(totalPatrimonio)).append("</span></div>");
        html.append("</div></div></td>");
        html.append("</tr>");
        html.append("</table>");
        
        html.append("<div class=\"observacao\">Observação: Este relatório foi gerado automaticamente pelo sistema de contabilidade.</div>");
        html.append("</body></html>");
        
        return html.toString();
    }

    private String formatarValor(BigDecimal valor) {
        if (valor == null) {
            return "0,00";
        }
        // Formatar para o padrão brasileiro (vírgula como separador decimal)
        String valorStr = valor.toString();
        if (valorStr.contains(".")) {
            String[] partes = valorStr.split("\\.");
            if (partes.length == 2) {
                String parteInteira = partes[0];
                String parteDecimal = partes[1];
                // Garantir que a parte decimal tenha 2 dígitos
                if (parteDecimal.length() == 1) {
                    parteDecimal += "0";
                } else if (parteDecimal.length() > 2) {
                    parteDecimal = parteDecimal.substring(0, 2);
                }
                return parteInteira + "," + parteDecimal;
            }
        }
        return valorStr + ",00";
    }
} 