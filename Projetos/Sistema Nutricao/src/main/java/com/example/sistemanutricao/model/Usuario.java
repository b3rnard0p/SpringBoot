package com.example.sistemanutricao.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    private Cargo cargo;

    private boolean ativo = false;

    private String caminhoImagem; // Substitua os campos de imagem por este

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "estabelecimento_id", nullable = true)
    private Estabelecimento estabelecimento;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Ingrediente> ingrediente;

    @OneToMany(mappedBy = "nutricionista", cascade = CascadeType.ALL)
    private List<FichaTecnica> fichaTecnicas;

    @OneToMany(mappedBy = "nutricionista", cascade = CascadeType.ALL)
    private List<Refeicao> refeicoes;

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public List<Ingrediente> getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(List<Ingrediente> ingrediente) {
        this.ingrediente = ingrediente;
    }

    public List<FichaTecnica> getFichaTecnicas() {
        return fichaTecnicas;
    }

    public void setFichaTecnicas(List<FichaTecnica> fichaTecnicas) {
        this.fichaTecnicas = fichaTecnicas;
    }

    public List<Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<Refeicao> refeicoes) {
        this.refeicoes = refeicoes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return ativo == usuario.ativo && Objects.equals(id, usuario.id) && Objects.equals(username, usuario.username) && Objects.equals(email, usuario.email) && Objects.equals(senha, usuario.senha) && cargo == usuario.cargo && Objects.equals(estabelecimento, usuario.estabelecimento) && Objects.equals(ingrediente, usuario.ingrediente) && Objects.equals(fichaTecnicas, usuario.fichaTecnicas) && Objects.equals(refeicoes, usuario.refeicoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, senha, cargo, ativo, estabelecimento, ingrediente, fichaTecnicas, refeicoes);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", cargo=" + cargo +
                ", ativo=" + ativo +
                ", estabelecimento=" + estabelecimento +
                ", fichaTecnicas=" + fichaTecnicas +
                ", refeicoes=" + refeicoes +
                '}';
    }
}
