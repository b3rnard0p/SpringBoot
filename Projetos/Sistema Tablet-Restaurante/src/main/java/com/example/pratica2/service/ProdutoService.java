package com.example.pratica2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pratica2.models.Produto;
import com.example.pratica2.models.Status;
import com.example.pratica2.records.ProdutoDTO.CreateProdutoDTO;
import com.example.pratica2.records.ProdutoDTO.GetProdutoDTO;
import com.example.pratica2.records.ProdutoDTO.UpdateProdutoDTO;
import com.example.pratica2.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public GetProdutoDTO createProduto(CreateProdutoDTO produtoDTO) {

        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoDTO.nome());
        novoProduto.setDescricao(produtoDTO.descricao());
        novoProduto.setPreco(produtoDTO.preco());
        novoProduto.setCategoria(produtoDTO.categoria());
        novoProduto.setStatus(produtoDTO.status());
        novoProduto.setImagemUrl(produtoDTO.imagemUrl());

        Produto produtoSalvo = produtoRepository.save(novoProduto);

        return convertToDto(produtoSalvo);
    }


    public Optional<GetProdutoDTO> getProdutoById(Long id) {
        return produtoRepository.findById(id)
                .map(produto -> new GetProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getCategoria(),
                        produto.getStatus(),
                        produto.getImagemUrl()
                ));
    }

    public Map<String, Map<String, List<GetProdutoDTO>>> getProdutosByStatus() {
        Map<String, Map<String, List<GetProdutoDTO>>> resultado = new HashMap<>();

        List<Produto> disponiveis = produtoRepository.findByStatus(Status.DISPONIVEL);
        Map<String, List<GetProdutoDTO>> disponiveisPorCategoria = groupByCategory(disponiveis);
        resultado.put("disponiveis", disponiveisPorCategoria);

        List<Produto> indisponiveis = produtoRepository.findByStatus(Status.INDISPONIVEL);
        Map<String, List<GetProdutoDTO>> indisponiveisPorCategoria = groupByCategory(indisponiveis);
        resultado.put("indisponiveis", indisponiveisPorCategoria);

        return resultado;
    }

    private Map<String, List<GetProdutoDTO>> groupByCategory(List<Produto> produtos) {
        Map<String, List<GetProdutoDTO>> porCategoria = new HashMap<>();

        produtos.forEach(produto -> {
            String nomeCategoria = produto.getCategoria().getNome();
            porCategoria.computeIfAbsent(nomeCategoria, k -> new ArrayList<>())
                    .add(convertToDto(produto));
        });

        return porCategoria;
    }

    private GetProdutoDTO convertToDto(Produto produto) {
        return new GetProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getStatus(),
                produto.getImagemUrl()
        );
    }

    public UpdateProdutoDTO updateProduto(Long id, UpdateProdutoDTO produtoDTO) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDTO.nome());
                    produto.setDescricao(produtoDTO.descricao());
                    produto.setPreco(produtoDTO.preco());
                    produto.setStatus(produtoDTO.status());
                    produto.setImagemUrl(produtoDTO.imagemUrl());

                    Produto updatedProduto = produtoRepository.save(produto);
                    return new UpdateProdutoDTO(
                            updatedProduto.getNome(),
                            updatedProduto.getDescricao(),
                            updatedProduto.getPreco(),
                            updatedProduto.getStatus(),
                            updatedProduto.getImagemUrl()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"
                ));
    }
}
