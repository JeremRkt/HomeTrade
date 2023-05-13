package com.isep.hometrade.map;

import com.isep.hometrade.business.AddressEntity;
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
public class HousingDto {

    @NotEmpty(message = "Type non valide !")
    private String type;

    @NotEmpty(message = "Nom non valide !")
    private String name;

    @NotEmpty(message = "Description non valide !")
    private String description;

    private AddressEntity addressEntity;

}
