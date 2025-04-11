package com.aula203.atividade1.controller;

import com.aula203.atividade1.records.CreateUserDTO;
import com.aula203.atividade1.records.DeleteUserDTO;
import com.aula203.atividade1.records.GetUserDTO;
import com.aula203.atividade1.records.UpdateUserDTO;
import com.aula203.atividade1.repository.UserRepository;
import com.aula203.atividade1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<GetUserDTO> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDTO> getById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public List<GetUserDTO> getUsersByNome(@PathVariable String nome) {
        return userService.findUsersByNome(nome);
    }

    @GetMapping("/nome/{nome}/email/{email}")
    public List<GetUserDTO> getUsersByNomeAndEmail(
            @PathVariable String nome,
            @PathVariable String email) {
        return userService.findUsersByNomeAndEmail(nome, email);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<GetUserDTO> getUserByEmail(@PathVariable String email) {
        try {
            GetUserDTO user = userService.findUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email-exists/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailExists(email));
    }

    @GetMapping("/search/{parteNome}")
    public List<GetUserDTO> searchUsersByPartialNome(@PathVariable String parteNome) {
        return userService.findUsersByPartialNome(parteNome);
    }

    @GetMapping("/domain/{dominio}")
    public List<GetUserDTO> getUsersByEmailDomain(@PathVariable String dominio) {
        return userService.findUsersByEmailDomain(dominio);
    }

    @GetMapping("/prefix/{prefixo}")
    public List<GetUserDTO> getUsersByNomePrefix(@PathVariable String prefixo) {
        return userService.findUsersByNomeStartingWith(prefixo);
    }

    @GetMapping("/ordered")
    public List<GetUserDTO> getAllUsersOrdered() {
        return userService.findAllUsersOrderedByNome();
    }

    @GetMapping("/top3")
    public List<GetUserDTO> getTop3Users() {
        return userService.findTop3UsersOrderedByNomeDesc();
    }

    @GetMapping("/single-phone")
    public List<GetUserDTO> getUsersWithSinglePhone() {
        return userService.findUsersWithSinglePhone();
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<GetUserDTO> create(@RequestBody CreateUserDTO userDTO) {
        GetUserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDTO> update(
            @PathVariable Long id,
            @RequestBody UpdateUserDTO userDTO) {

        GetUserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestBody DeleteUserDTO deleteDTO) {
        if (!id.equals(deleteDTO.id())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID do path não corresponde ao ID do corpo da requisição"
            );
        }

        userService.deleteUser(deleteDTO);
        return ResponseEntity.noContent().build();
    }
}