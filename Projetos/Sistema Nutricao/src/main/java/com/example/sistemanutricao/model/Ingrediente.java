package com.example.sistemanutricao.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String nome;

    private BigDecimal ptn;

    private BigDecimal cho;

    private BigDecimal lip;

    private BigDecimal sodio;

    private BigDecimal gorduraSaturada;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "ingrediente", cascade = CascadeType.ALL)
    private List<IngredientesPorFicha> ingredientesPorFicha;

    public Ingrediente() {
    }

    public Ingrediente(Long id, String nome, BigDecimal ptn, BigDecimal cho, BigDecimal lip, BigDecimal sodio, BigDecimal gorduraSaturada,
                       Status status, Usuario usuario, List<IngredientesPorFicha> ingredientesPorFicha) {
        this.id = id;
        this.nome = nome;
        this.ptn = ptn;
        this.cho = cho;
        this.lip = lip;
        this.sodio = sodio;
        this.gorduraSaturada = gorduraSaturada;
        this.status = status;
        this.usuario = usuario;
        this.ingredientesPorFicha = ingredientesPorFicha;
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

    public BigDecimal getPtn() {
        return ptn;
    }

    public void setPtn(BigDecimal ptn) {
        this.ptn = ptn;
    }

    public BigDecimal getCho() {
        return cho;
    }

    public void setCho(BigDecimal cho) {
        this.cho = cho;
    }

    public BigDecimal getLip() {
        return lip;
    }

    public void setLip(BigDecimal lip) {
        this.lip = lip;
    }

    public BigDecimal getSodio() {
        return sodio;
    }

    public void setSodio(BigDecimal sodio) {
        this.sodio = sodio;
    }

    public BigDecimal getGorduraSaturada() {
        return gorduraSaturada;
    }

    public void setGorduraSaturada(BigDecimal gorduraSaturada) {
        this.gorduraSaturada = gorduraSaturada;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<IngredientesPorFicha> getIngredientesPorFicha() {
        return ingredientesPorFicha;
    }

    public void setIngredientesPorFicha(List<IngredientesPorFicha> ingredientesPorFicha) {
        this.ingredientesPorFicha = ingredientesPorFicha;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(ptn, that.ptn) && Objects.equals(cho, that.cho) && Objects.equals(lip, that.lip) && Objects.equals(sodio, that.sodio) && Objects.equals(gorduraSaturada, that.gorduraSaturada) && status == that.status && Objects.equals(usuario, that.usuario) && Objects.equals(ingredientesPorFicha, that.ingredientesPorFicha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, ptn, cho, lip, sodio, gorduraSaturada, status, usuario, ingredientesPorFicha);
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", ptn=" + ptn +
                ", cho=" + cho +
                ", lip=" + lip +
                ", sodio=" + sodio +
                ", gorduraSaturada=" + gorduraSaturada +
                ", status=" + status +
                ", usuario=" + (usuario != null ? usuario.getId() : "null") +
                ", ingredientesPorFicha=" + (ingredientesPorFicha != null ? ingredientesPorFicha.size() + " items" : "null") +
                '}';
    }
}
