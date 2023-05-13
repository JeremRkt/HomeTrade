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

    @NotEmpty(message = "Numéro non valide !")
    private String number;

    @NotEmpty(message = "Rue non valide !")
    private String street;

    @NotEmpty(message = "Ville non valide !")
    private String city;

    @NotEmpty(message = "Code postal non valide !")
    private String code;

    @NotEmpty(message = "Pays non valide !")
    private String country;

}
