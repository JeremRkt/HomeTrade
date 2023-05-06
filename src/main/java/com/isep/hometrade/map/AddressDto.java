package com.isep.hometrade.map;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotEmpty(message = "Le numéro saisi n'est pas valide !")
    private String number;

    @NotEmpty(message = "La rue saisie n'est pas valide !")
    private String street;

    @NotEmpty(message = "La ville saisie n'est pas valide !")
    private String city;

    @NotEmpty(message = "Le code postal saisi n'est pas valide !")
    private String code;

    @NotEmpty(message = "Le pays saisi n'est pas valide !")
    private String country;

}
