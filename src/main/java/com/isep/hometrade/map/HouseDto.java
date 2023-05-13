package com.isep.hometrade.map;

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

    @NotEmpty(message = "Nom non valide !")
    private String name;

    @NotEmpty(message = "Description non valide !")
    private String description;

    private AddressEntity addressEntity;

}
