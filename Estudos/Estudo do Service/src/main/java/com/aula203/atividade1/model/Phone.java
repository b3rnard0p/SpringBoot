package com.aula203.atividade1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public Phone(Long id, String number, User user) {
        this.id = id;
        this.number = number;
        this.user = user;
    }

    public Phone(String number, User user) {
        this.number = number;
        this.user = user;
    }

    public Phone() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


