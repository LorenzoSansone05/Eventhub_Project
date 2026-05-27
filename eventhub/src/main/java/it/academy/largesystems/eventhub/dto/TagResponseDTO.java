package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rappresentazione del tag restituita dal server")
public class TagResponseDTO {

    @Schema(description = "ID univoco del tag salvato nel database", example = "3")
    private Long id;

    @Schema(description = "Nome del tag", example = "Programmazione")
    private String name;
}