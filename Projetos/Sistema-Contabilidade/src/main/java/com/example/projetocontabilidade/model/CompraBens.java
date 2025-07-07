package com.example.projetocontabilidade.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class CompraBens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataCompra;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bem_id", nullable = false)
    private Bem bem;

    private BigDecimal valorTotal;

    private int quantidade;

    @OneToMany(mappedBy = "compraBens", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaBens> parcelas;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoPagamento tipoPagamento;

    public CompraBens() {
    }

    public List<ParcelaBens> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ParcelaBens> parcelas) {
        this.parcelas = parcelas;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Bem getBem() {
        return bem;
    }

    public void setBem(Bem bem) {
        this.bem = bem;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompraBens that = (CompraBens) o;
        return quantidade == that.quantidade && Objects.equals(id, that.id) && Objects.equals(dataCompra, that.dataCompra) && Objects.equals(bem, that.bem) && Objects.equals(valorTotal, that.valorTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataCompra, bem, valorTotal, quantidade);
    }

    @Override
    public String toString() {
        return "CompraBens{" +
                "id=" + id +
                ", dataCompra=" + dataCompra +
                ", bem=" + bem +
                ", valorTotal=" + valorTotal +
                ", quantidade=" + quantidade +
                '}';
    }
}
