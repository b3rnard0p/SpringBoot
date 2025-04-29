package com.example.pratica2.records.ProdutoDTO;

import com.example.pratica2.models.Categoria;
import com.example.pratica2.models.Status;

public record UpdateProdutoDTO (String nome, String descricao, double preco, Status status, String imagemUrl){
}
