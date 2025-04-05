package com.aula203.atividade1.service;

import com.aula203.atividade1.records.CreateUserDTO;
import com.aula203.atividade1.model.User;
import com.aula203.atividade1.records.GetUserDTO;
import com.aula203.atividade1.records.UpdateUserDTO;
import com.aula203.atividade1.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO userDTO) {
        User newUser = new User();
        newUser.setNome(userDTO.nome());
        newUser.setEmail(userDTO.email());
        newUser.setSenha(userDTO.senha());

        return userRepository.save(newUser);
    }

    public List<GetUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail()
                ))
                .toList();
    }

    public Optional<GetUserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail()
                ));
    }

    public GetUserDTO updateUser(Long id, UpdateUserDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userDTO.nome() != null) {
                        user.setNome(userDTO.nome());
                    }
                    if (userDTO.email() != null) {
                        user.setEmail(userDTO.email());
                    }

                    User updatedUser = userRepository.save(user);
                    return new GetUserDTO(
                            updatedUser.getId(),
                            updatedUser.getNome(),
                            updatedUser.getEmail()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }
}