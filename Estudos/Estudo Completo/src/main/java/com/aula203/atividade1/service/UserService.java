package com.aula203.atividade1.service;

import com.aula203.atividade1.model.Phone;
import com.aula203.atividade1.model.User;
import com.aula203.atividade1.records.CreateUserDTO;
import com.aula203.atividade1.records.DeleteUserDTO;
import com.aula203.atividade1.records.GetUserDTO;
import com.aula203.atividade1.records.UpdateUserDTO;
import com.aula203.atividade1.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios relacionada a usuários.
 * Gerencia operações de criação, consulta, atualização e remoção de usuários.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Construtor para injeção de dependências.
     *
     * @param userRepository Repositório de operações para usuários
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param userDTO DTO contendo os dados do usuário a ser criado
     * @return DTO com os dados do usuário criado
     */
    public GetUserDTO createUser(CreateUserDTO userDTO) {
        User newUser = new User();
        newUser.setNome(userDTO.nome());
        newUser.setEmail(userDTO.email());
        newUser.setSenha(userDTO.senha());

        List<Phone> phones = userDTO.phones().stream()
                .map(phoneDTO -> {
                    Phone phone = new Phone();
                    phone.setNumber(phoneDTO.number());
                    phone.setUser(newUser);
                    return phone;
                })
                .toList();

        newUser.setPhones(phones);
        User savedUser = userRepository.save(newUser);

        return new GetUserDTO(
                savedUser.getId(),
                savedUser.getNome(),
                savedUser.getEmail(),
                savedUser.getPhones()
        );
    }

    /**
     * Recupera todos os usuários cadastrados no sistema.
     *
     * @return Lista de DTOs com todos os usuários
     */
    public List<GetUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Busca um usuário específico pelo ID.
     *
     * @param id ID do usuário a ser buscado
     * @return Optional contendo o DTO do usuário, se encontrado
     */
    public Optional<GetUserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ));
    }

    /**
     * Busca usuários pelo nome exato.
     *
     * @param nome Nome completo do usuário
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersByNome(String nome) {
        return userRepository.findByNome(nome).stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Busca usuários por nome e email combinados.
     *
     * @param nome Nome do usuário
     * @param email Email do usuário
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersByNomeAndEmail(String nome, String email) {
        return userRepository.findByNomeAndEmail(nome, email).stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Busca um usuário pelo email.
     *
     * @param email Email do usuário
     * @return DTO com os dados do usuário encontrado
     * @throws ResponseStatusException Se o usuário não for encontrado
     */
    public GetUserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuário não encontrado com email: " + email
            );
        }
        return new GetUserDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getPhones()
        );
    }

    /**
     * Verifica se um email já está cadastrado no sistema.
     *
     * @param email Email a ser verificado
     * @return true se o email existe, false caso contrário
     */
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Busca usuários por parte do nome.
     *
     * @param parteNome Parte do nome a ser pesquisada
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersByPartialNome(String parteNome) {
        return userRepository.findByNomeContaining(parteNome).stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Busca usuários por domínio de email.
     *
     * @param dominio Domínio do email (ex: "gmail.com")
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersByEmailDomain(String dominio) {
        return userRepository.findByEmailContaining(dominio).stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Busca usuários cujo nome começa com determinado prefixo.
     *
     * @param prefixo Prefixo do nome
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersByNomeStartingWith(String prefixo) {
        return userRepository.findByNomeStartingWith(prefixo).stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Recupera todos os usuários ordenados alfabeticamente por nome.
     *
     * @return Lista ordenada de DTOs com os usuários
     */
    public List<GetUserDTO> findAllUsersOrderedByNome() {
        return userRepository.findAllByOrderByNomeAsc().stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Recupera os 3 últimos usuários cadastrados, ordenados por nome em ordem decrescente.
     *
     * @return Lista com os 3 usuários mais recentes
     */
    public List<GetUserDTO> findTop3UsersOrderedByNomeDesc() {
        return userRepository.findTop3ByOrderByNomeDesc().stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Recupera usuários que possuem exatamente um telefone cadastrado.
     *
     * @return Lista de DTOs com os usuários encontrados
     */
    public List<GetUserDTO> findUsersWithSinglePhone() {
        return userRepository.findUsersWithSinglePhone().stream()
                .map(user -> new GetUserDTO(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getPhones()
                ))
                .toList();
    }

    /**
     * Remove um usuário do sistema pelo email.
     *
     * @param email Email do usuário a ser removido
     * @throws ResponseStatusException Se o usuário não for encontrado
     */
    public void deleteUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuário não encontrado com email: " + email
            );
        }
        userRepository.deleteByEmail(email);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id ID do usuário a ser atualizado
     * @param userDTO DTO contendo os novos dados do usuário
     * @return DTO com os dados atualizados do usuário
     * @throws ResponseStatusException Se o usuário não for encontrado
     */
    public GetUserDTO updateUser(Long id, UpdateUserDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    // Atualiza nome se fornecido
                    if (userDTO.nome() != null) {
                        user.setNome(userDTO.nome());
                    }

                    // Atualiza email se fornecido
                    if (userDTO.email() != null) {
                        user.setEmail(userDTO.email());
                    }

                    // Persiste as alterações
                    User updatedUser = userRepository.save(user);

                    // Retorna DTO atualizado
                    return new GetUserDTO(
                            updatedUser.getId(),
                            updatedUser.getNome(),
                            updatedUser.getEmail(),
                            user.getPhones()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }

    /**
     * Remove um usuário do sistema com confirmação.
     *
     * @param deleteDTO DTO contendo ID e confirmação para exclusão
     * @throws ResponseStatusException Se:
     *         - A confirmação não for válida (400 BAD_REQUEST)
     *         - O usuário não for encontrado (404 NOT_FOUND)
     */
    public void deleteUser(DeleteUserDTO deleteDTO) {
        // Valida confirmação
        if (!"CONFIRMAR".equalsIgnoreCase(deleteDTO.confirmation())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Confirmação de exclusão necessária. Envie 'CONFIRMAR' no campo confirmation."
            );
        }

        User user = userRepository.findById(deleteDTO.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        userRepository.delete(user);
    }
}