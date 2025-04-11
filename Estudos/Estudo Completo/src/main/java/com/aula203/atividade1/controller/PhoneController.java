package com.aula203.atividade1.controller;

import com.aula203.atividade1.records.CreatePhoneDTO;
import com.aula203.atividade1.records.DeletePhoneDTO;
import com.aula203.atividade1.records.GetPhoneDTO;
import com.aula203.atividade1.records.UpdatePhoneDTO;
import com.aula203.atividade1.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping
    public List<GetPhoneDTO> getAll() {
        return phoneService.getAllPhones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPhoneDTO> getById(@PathVariable Long id) {
        return phoneService.getPhoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{nome}")
    public List<GetPhoneDTO> getPhonesByUserName(@PathVariable String nome) {
        return phoneService.findPhonesByUserName(nome);
    }

    @GetMapping("/user/{userId}/number/{prefixo}")
    public List<GetPhoneDTO> getPhonesByUserAndNumberStartingWith(
            @PathVariable Long userId,
            @PathVariable String prefixo) {
        return phoneService.findPhonesByUserAndNumberStartingWith(userId, prefixo);
    }

    @PostMapping
    public ResponseEntity<GetPhoneDTO> create(@RequestBody CreatePhoneDTO phoneDTO) {
        GetPhoneDTO createdPhone = phoneService.createPhone(phoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPhoneDTO> update(
            @PathVariable Long id,
            @RequestBody UpdatePhoneDTO phoneDTO) {
        GetPhoneDTO updatedPhone = phoneService.updatePhone(id, phoneDTO);
        return ResponseEntity.ok(updatedPhone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestBody DeletePhoneDTO deleteDTO) {
        if (!id.equals(deleteDTO.id())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID do path não corresponde ao ID do corpo da requisição"
            );
        }

        phoneService.deletePhone(deleteDTO);
        return ResponseEntity.noContent().build();
    }
}