package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dati richiesti per il censimento o la modifica di un relatore")
public class SpeakerRequestDTO {

    @NotBlank(message = "Il nome del relatore è obbligatorio.")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri.")
    @Schema(description = "Nome dello speaker", example = "Jane", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Il cognome del relatore è obbligatorio.")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri.")
    @Schema(description = "Cognome dello speaker", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String surname;

    @Size(max = 1000, message = "La biografia non può superare i 1000 caratteri.")
    @Schema(description = "Profilo professionale o biografia dello speaker",
            example = "Esperta internazionale di architetture a microservizi e Cloud Native computing. Autrice di diversi libri sul tema.",
            nullable = true)
    private String bio;
}