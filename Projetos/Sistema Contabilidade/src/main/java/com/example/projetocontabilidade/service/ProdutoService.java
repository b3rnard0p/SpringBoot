package com.example.projetocontabilidade.service;

import com.example.projetocontabilidade.model.Produto;
import com.example.projetocontabilidade.records.ProdutoDTO.CreateProdutoDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.GetProdutoDTO;
import com.example.projetocontabilidade.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.projetocontabilidade.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto save(CreateProdutoDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.nome());
        produto.setPrecoCompra(produtoDTO.precoCompra());
        produto.setPrecoVenda(produtoDTO.precoVenda());
        produto.setQuantidade(produtoDTO.quantidade());
        return produtoRepository.save(produto);
    }

    public Produto getProdutoEntityById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<GetProdutoDTO> getAllProdutos() {
        return produtoRepository.findAll().stream()
                .map(produto -> new GetProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getPrecoCompra(),
                        produto.getPrecoVenda(),
                        produto.getQuantidade()
                ))
                .toList();
    }

    public Optional<GetProdutoDTO> getByIdProduto(Long id) {
        return produtoRepository.findById(id)
                .map(produto -> new GetProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getPrecoCompra(),
                        produto.getPrecoVenda(),
                        produto.getQuantidade()
                ));
    }
    public UpdateProdutoDTO update(Long id, UpdateProdutoDTO produtoDTO) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDTO.nome());
                    produto.setPrecoCompra(produtoDTO.precoCompra());
                    produto.setPrecoVenda(produtoDTO.precoVenda());


                    if (produtoDTO.quantidade() != null) {
                        produto.setQuantidade(produtoDTO.quantidade());
                    }

                    Produto updatedproduto = produtoRepository.save(produto);

                    return new UpdateProdutoDTO(
                            updatedproduto.getNome(),
                            updatedproduto.getPrecoCompra(),
                            updatedproduto.getPrecoVenda(),
                            updatedproduto.getQuantidade()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));
    }

}