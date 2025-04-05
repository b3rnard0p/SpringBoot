package com.aula203.atividade1.service;

import com.aula203.atividade1.Repository.UserRepository;
import com.aula203.atividade1.model.Phone;
import com.aula203.atividade1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validarQtdEmail(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("O e-mail " + user.getEmail() + " já está cadastrado para outro usuário");
        }
    }

    private void validarPhone(Phone phone, User user) {
        if (user.getPhones().size() >= 3) {
            throw new IllegalArgumentException("Limite de telefones excedido. Um usuário pode ter no máximo 3 telefones cadastrados.");
        }

        String number = phone.getNumber();
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de telefone não pode estar vazio");
        }

        String digitsOnly = number.replaceAll("[^0-9]", "");
        if (digitsOnly.length() < 10) {
            throw new IllegalArgumentException("Número de telefone deve conter DDD + 8 dígitos");
        }

        int ddd;

        try {
            ddd = Integer.parseInt(digitsOnly.substring(0, 2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de DDD inválido");
        }

        if (ddd < 11 || ddd > 99) {
            throw new IllegalArgumentException("DDD deve estar entre 11 e 99");
        }

        boolean numeroJaExiste = user.getPhones().stream()
                .anyMatch(p -> p.getNumber().replaceAll("[^0-9]", "").equals(digitsOnly));

        if (numeroJaExiste) {
            throw new IllegalArgumentException("Este número de telefone já está cadastrado para este usuário");
        }
    }

    @Transactional
    public User createUser(User user) {
        validarQtdEmail(user);

        if (user.getNome() == null || user.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório");
        }

        User savedUser = userRepository.save(user);

        if (user.getPhones() != null) {
            List<Phone> phonesToAdd = new ArrayList<>(user.getPhones());
            savedUser.getPhones().clear();

            for (Phone phone : phonesToAdd) {
                validarPhone(phone, savedUser);
                phone.setUser(savedUser);
                savedUser.getPhones().add(phone);
            }
        }

        return userRepository.save(savedUser);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    validarQtdEmail(updatedUser);

                    user.setNome(updatedUser.getNome());
                    user.setEmail(updatedUser.getEmail());

                    user.getPhones().clear();
                    if (updatedUser.getPhones() != null) {
                        for (Phone phone : updatedUser.getPhones()) {
                            validarPhone(phone, user);
                            phone.setUser(user);
                            user.getPhones().add(phone);
                        }
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }
}