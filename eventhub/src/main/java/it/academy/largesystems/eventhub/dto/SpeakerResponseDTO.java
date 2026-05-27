package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rappresentazione dati del relatore restituita dal server")
public class SpeakerResponseDTO {

    @Schema(description = "ID univoco del relatore", example = "12")
    private Long id;

    @Schema(description = "Nome dello speaker", example = "Jane")
    private String name;

    @Schema(description = "Cognome dello speaker", example = "Doe")
    private String surname;

    @Schema(description = "Biografia dello speaker", example = "Esperta internazionale di architetture a microservizi...")
    private String bio;
}