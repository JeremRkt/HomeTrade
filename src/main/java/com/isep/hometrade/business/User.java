package com.isep.hometrade.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long idUser;
    @NotEmpty(message = "Le nom saisi n'est pas valide !")
    private String firstname;
    @NotEmpty(message = "Le prénom saisi n'est pas valide !")
    private String lastname;
    @NotEmpty(message = "L'adresse e-mail saisie n'est pas valide !")
    @Email
    private String email;
    @NotEmpty(message = "Le mot de passe saisi n'est pas valide !")
    private String password;

    public User(){
    }

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}