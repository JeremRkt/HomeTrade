package com.isep.hometrade.util;

import com.isep.hometrade.business.AddressEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {

    private Integer idHome;

    @NotEmpty(message = "L'intitulé saisi n'est pas valide !")
    private String title;

    @NotEmpty(message = "La description saisie n'est pas valide !")
    private String description;

    @NotEmpty(message = "La photo saisie n'est pas valide !")
    private String photo;

    private AddressEntity addressEntity;

}
