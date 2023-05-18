package com.isep.hometrade.map;

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

    @NotEmpty(message = "Le prénom renseigné n''est pas valide !")
    private String firstname;

    @NotEmpty(message = "Le nom renseigné n''est pas valide !")
    private String lastname;

    @NotEmpty(message = "L''adresse e-mail renseignée n''est pas valide !")
    @Email
    private String email;

    @NotEmpty(message = "Le mot de passe renseigné n''est pas valide !")
    private String password;

}
