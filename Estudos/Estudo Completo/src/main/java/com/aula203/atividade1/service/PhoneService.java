package com.aula203.atividade1.service;

import com.aula203.atividade1.model.Phone;
import com.aula203.atividade1.model.User;
import com.aula203.atividade1.records.CreatePhoneDTO;
import com.aula203.atividade1.records.DeletePhoneDTO;
import com.aula203.atividade1.records.GetPhoneDTO;
import com.aula203.atividade1.records.UpdatePhoneDTO;
import com.aula203.atividade1.repository.PhoneRepository;
import com.aula203.atividade1.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar operações relacionadas a telefones.
 * Contém a lógica de negócios para criação, recuperação, atualização e exclusão de telefones.
 */
@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    /**
     * Construtor para injeção de dependências.
     *
     * @param phoneRepository Repositório de operações para telefones
     * @param userRepository Repositório de operações para usuários
     */
    public PhoneService(PhoneRepository phoneRepository, UserRepository userRepository) {
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo telefone no sistema.
     *
     * @param phoneDTO DTO contendo os dados do telefone a ser criado
     * @return DTO com os dados do telefone criado
     * @throws ResponseStatusException Se o usuário associado não for encontrado
     */
    public GetPhoneDTO createPhone(CreatePhoneDTO phoneDTO) {
        // Busca o usuário associado
        User user = userRepository.findById(phoneDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado com ID: " + phoneDTO.userId()
                ));

        // Cria nova entidade Phone
        Phone newPhone = new Phone();
        newPhone.setNumber(phoneDTO.number());
        newPhone.setUser(user);

        // Persiste o telefone
        Phone savedPhone = phoneRepository.save(newPhone);

        // Retorna DTO com os dados persistidos
        return new GetPhoneDTO(
                savedPhone.getId(),
                savedPhone.getNumber(),
                savedPhone.getUser().getId()
        );
    }

    /**
     * Recupera todos os telefones cadastrados no sistema.
     *
     * @return Lista de DTOs com todos os telefones
     */
    public List<GetPhoneDTO> getAllPhones() {
        return phoneRepository.findAll().stream()
                .map(phone -> new GetPhoneDTO(
                        phone.getId(),
                        phone.getNumber(),
                        phone.getUser().getId()
                ))
                .toList();
    }

    /**
     * Busca um telefone específico pelo ID.
     *
     * @param id ID do telefone a ser buscado
     * @return Optional contendo o DTO do telefone, se encontrado
     */
    public Optional<GetPhoneDTO> getPhoneById(Long id) {
        return phoneRepository.findById(id)
                .map(phone -> new GetPhoneDTO(
                        phone.getId(),
                        phone.getNumber(),
                        phone.getUser().getId()
                ));
    }

    /**
     * Busca telefones associados a um usuário pelo nome.
     *
     * @param nome Nome do usuário associado aos telefones
     * @return Lista de DTOs com os telefones encontrados
     */
    public List<GetPhoneDTO> findPhonesByUserName(String nome) {
        return phoneRepository.findByUserName(nome).stream()
                .map(phone -> new GetPhoneDTO(
                        phone.getId(),
                        phone.getNumber(),
                        phone.getUser().getId()
                ))
                .toList();
    }

    /**
     * Busca telefones de um usuário específico que começam com determinado prefixo.
     *
     * @param userId ID do usuário
     * @param prefixo Prefixo do número de telefone
     * @return Lista de DTOs com os telefones encontrados
     */
    public List<GetPhoneDTO> findPhonesByUserAndNumberStartingWith(Long userId, String prefixo) {
        return phoneRepository.findByUserAndNumberStartingWith(userId, prefixo).stream()
                .map(phone -> new GetPhoneDTO(
                        phone.getId(),
                        phone.getNumber(),
                        phone.getUser().getId()
                ))
                .toList();
    }

    /**
     * Atualiza os dados de um telefone existente.
     *
     * @param id ID do telefone a ser atualizado
     * @param phoneDTO DTO contendo os novos dados do telefone
     * @return DTO com os dados atualizados do telefone
     * @throws ResponseStatusException Se o telefone ou usuário não forem encontrados
     */
    public GetPhoneDTO updatePhone(Long id, UpdatePhoneDTO phoneDTO) {
        return phoneRepository.findById(id)
                .map(phone -> {
                    // Atualiza número se fornecido
                    if (phoneDTO.number() != null) {
                        phone.setNumber(phoneDTO.number());
                    }

                    // Atualiza usuário se fornecido
                    if (phoneDTO.userId() != null) {
                        User user = userRepository.findById(phoneDTO.userId())
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User not found"
                                ));
                        phone.setUser(user);
                    }

                    // Persiste as alterações
                    Phone updatedPhone = phoneRepository.save(phone);

                    // Retorna DTO atualizado
                    return new GetPhoneDTO(
                            updatedPhone.getId(),
                            updatedPhone.getNumber(),
                            updatedPhone.getUser().getId()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Phone not found"
                ));
    }

    /**
     * Remove um telefone do sistema.
     *
     * @param deleteDTO DTO contendo ID e confirmação para exclusão
     * @throws ResponseStatusException Se:
     *         - A confirmação não for válida (400 BAD_REQUEST)
     *         - O telefone não for encontrado (404 NOT_FOUND)
     */
    public void deletePhone(DeletePhoneDTO deleteDTO) {
        // Valida confirmação
        if (!"CONFIRMAR".equalsIgnoreCase(deleteDTO.confirmation())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Confirmação de exclusão necessária. Envie 'CONFIRMAR' no campo confirmation."
            );
        }

        Phone phone = phoneRepository.findById(deleteDTO.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Phone not found"
                ));

        phoneRepository.delete(phone);
    }
}