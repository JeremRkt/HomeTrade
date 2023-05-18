package com.isep.hometrade.map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotEmpty(message = "Le numéro renseigné n''est pas valide !")
    private String number;

    @NotEmpty(message = "La rue renseignée n''est pas valide !")
    private String street;

    @NotEmpty(message = "La ville renseignée n''est pas valide !")
    private String city;

    @NotEmpty(message = "Le code postal renseigné n''est pas valide !")
    private String code;

    @NotEmpty(message = "La pays renseigné n''est pas valide !")
    private String country;

}
