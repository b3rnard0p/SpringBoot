package com.aula203.atividade1.controller;

import com.aula203.atividade1.Repository.UserRepository;
import com.aula203.atividade1.model.User;
import com.aula203.atividade1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(201).body(
                    Map.of(
                            "status", "success",
                            "message", "Usuário criado com sucesso",
                            "data", createdUser
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "error",
                            "message", "Falha ao criar usuário",
                            "reason", e.getMessage()
                    )
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(
                    Map.of(
                            "status", "success",
                            "message", "Usuário atualizado com sucesso",
                            "data", user
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "error",
                            "message", "Falha ao atualizar usuário",
                            "reason", e.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(
                    Map.of(
                            "status", "success",
                            "message", "Usuário deletado com sucesso"
                    )
            );
        }
        return ResponseEntity.status(404).body(
                Map.of(
                        "status", "error",
                        "message", "Usuário não encontrado com ID: " + id
                )
        );
    }
}