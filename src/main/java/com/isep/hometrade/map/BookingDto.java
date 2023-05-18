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
public class BookingDto {

    @NotNull(message = "La date de début renseignée n''est pas valide !")
    private String start;

    @NotEmpty(message = "La date de fin renseignée n''est pas valide !")
    private String end;

    @NotEmpty(message = "Le nombre d''adultes renseigné n''est pas valide !")
    private String adults;

    @NotEmpty(message = "Le nombre d''enfants renseigné n''est pas valide !")
    private String children;

}
