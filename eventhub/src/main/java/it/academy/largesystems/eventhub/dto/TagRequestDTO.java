package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Payload per la creazione o l'aggiornamento del nome di un tag")
public class TagRequestDTO {

    @NotBlank(message = "Il nome del tag è obbligatorio.")
    @Size(max = 50, message = "Il tag non può superare i 50 caratteri.")
    @Schema(description = "Nome identificativo del tag", example = "Programmazione", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}