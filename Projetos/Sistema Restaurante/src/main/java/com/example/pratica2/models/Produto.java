package com.example.pratica2.models;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(length = 2000)
    private String descricao;

    private double preco;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String imagemUrl;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ItensComanda> ItensComanda;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ItensCarrinho> ItensCarrinho;

    public Produto() {
    }

    public Produto(Long id, String descricao, double preco, Categoria categoria, Status status, String imagemUrl, List<com.example.pratica2.models.ItensComanda> itensComanda, List<com.example.pratica2.models.ItensCarrinho> itensCarrinho) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.status = status;
        this.imagemUrl = imagemUrl;
        ItensComanda = itensComanda;
        ItensCarrinho = itensCarrinho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public List<ItensComanda> getItensComanda() {
        return ItensComanda;
    }

    public void setItensComanda(List<ItensComanda> itensComanda) {
        ItensComanda = itensComanda;
    }

    public List<ItensCarrinho> getItensCarrinho() {
        return ItensCarrinho;
    }

    public void setItensCarrinho(List<ItensCarrinho> itensCarrinho) {
        ItensCarrinho = itensCarrinho;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Double.compare(preco, produto.preco) == 0 && Objects.equals(id, produto.id) && Objects.equals(nome, produto.nome) && Objects.equals(descricao, produto.descricao) && categoria == produto.categoria && status == produto.status && Objects.equals(imagemUrl, produto.imagemUrl) && Objects.equals(ItensComanda, produto.ItensComanda) && Objects.equals(ItensCarrinho, produto.ItensCarrinho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, preco, categoria, status, imagemUrl, ItensComanda, ItensCarrinho);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", categoria=" + categoria +
                ", status=" + status +
                ", imagemUrl='" + imagemUrl + '\'' +
                ", ItensComanda=" + ItensComanda +
                ", ItensCarrinho=" + ItensCarrinho +
                '}';
    }
}
