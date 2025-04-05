package com.bernardo.springcrud.controller;

import com.bernardo.springcrud.model.Pessoa;
import com.bernardo.springcrud.repository.PessoaReposiroty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaReposiroty pessoaReposiroty;

    @Autowired
    public PessoaController(PessoaReposiroty pessoaReposiroty) {
        this.pessoaReposiroty = pessoaReposiroty;
    }

    @GetMapping
    public List<Pessoa> listarPessoas() {
        return pessoaReposiroty.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPorId(@PathVariable int id ){
        return pessoaReposiroty.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Pessoa> buscarPorCpf(@PathVariable String cpf ){
        return pessoaReposiroty.findByCpf(cpf).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable int id, @RequestBody Pessoa pessoaAtualizada) {
        Pessoa pessoa = pessoaReposiroty.findById(id)
                .orElseThrow(()-> new RuntimeException("Pessoa não encontrada com id: " + id));
        pessoa.setNome(pessoaAtualizada.getNome());
        pessoa.setNome(pessoaAtualizada.getCpf());
        pessoa.setNome(pessoaAtualizada.getEmail());
        pessoaReposiroty.save(pessoa);
        return ResponseEntity.ok(pessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pessoa> remover(@PathVariable int id) {
        if(pessoaReposiroty.existsById(id)) {
            pessoaReposiroty.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public Pessoa salvar(@RequestBody Pessoa pessoa) {
        pessoaReposiroty.save(pessoa);
        return pessoa;
    }
}
