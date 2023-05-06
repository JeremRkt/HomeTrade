package com.isep.hometrade.map;

import com.isep.hometrade.business.AddressEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {

    @NotEmpty(message = "L'intitulé saisi n'est pas valide !")
    private String title;

    @NotEmpty(message = "La description saisie n'est pas valide !")
    private String description;

    private AddressEntity addressEntity;

    private List<MultipartFile> photoEntities;

}
