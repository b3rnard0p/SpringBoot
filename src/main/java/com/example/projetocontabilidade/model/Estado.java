package com.example.projetocontabilidade.model;

import java.math.BigDecimal;

public enum Estado {
    AC(17.0, "Acre"),
    AL(18.0, "Alagoas"),
    AM(18.0, "Amazonas"),
    AP(18.0, "Amapá"),
    BA(18.0, "Bahia"),
    CE(18.0, "Ceará"),
    DF(18.0, "Distrito Federal"),
    ES(17.0, "Espírito Santo"),
    GO(17.0, "Goiás"),
    MA(18.0, "Maranhão"),
    MG(18.0, "Minas Gerais"),
    MS(17.0, "Mato Grosso do Sul"),
    MT(17.0, "Mato Grosso"),
    PA(17.0, "Pará"),
    PB(18.0, "Paraíba"),
    PE(18.0, "Pernambuco"),
    PI(18.0, "Piauí"),
    PR(18.0, "Paraná"),
    RJ(20.0, "Rio de Janeiro"),  // Exemplo: RJ com alíquota diferente
    RN(18.0, "Rio Grande do Norte"),
    RO(17.5, "Rondônia"),       // Exemplo: RO com alíquota intermediária
    RR(17.0, "Roraima"),
    RS(18.0, "Rio Grande do Sul"),
    SC(17.0, "Santa Catarina"),
    SE(18.0, "Sergipe"),
    SP(18.0, "São Paulo"),
    TO(17.0, "Tocantins");

    private final double aliquota;
    private final String nome;

    Estado(double aliquota, String nome) {
        this.aliquota = aliquota;
        this.nome = nome;
    }

    public double getAliquota() {
        return aliquota;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getAliquotaAsBigDecimal() {
        return BigDecimal.valueOf(aliquota).divide(BigDecimal.valueOf(100));
    }
}