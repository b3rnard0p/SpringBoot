package com.example.pratica2.records.ProdutoDTO;

import com.example.pratica2.models.Categoria;
import com.example.pratica2.models.Status;

public record CreateProdutoDTO (String nome, String descricao, double preco, Categoria categoria, Status status, String imagemUrl){
}
