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
public class MessageDto {

    @NotEmpty(message = "Le contenu n''est pas valide !")
    private String content;

}
