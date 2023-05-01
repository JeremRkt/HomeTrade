package com.isep.hometrade.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int idUser;

    @NotEmpty(message = "Le prénom saisi n'est pas valide !")
    private String firstname;

    @NotEmpty(message = "Le nom saisi n'est pas valide !")
    private String lastname;

    @NotEmpty(message = "L'adresse e-mail saisie n'est pas valide !")
    @Email
    private String email;

    @NotEmpty(message = "Le mot de passe saisi n'est pas valide")
    private String password;

    private Integer type;

}
