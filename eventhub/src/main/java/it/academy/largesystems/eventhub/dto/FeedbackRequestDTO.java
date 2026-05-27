package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload per la creazione o modifica di una recensione")
public class FeedbackRequestDTO {

    @Min(value = 1, message = "Il voto minimo è 1")
    @Max(value = 5, message = "Il voto massimo è 5")
    @Schema(description = "Valutazione numerica dell'evento (da 1 a 5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private int rating;

    @Size(max = 500, message = "Il testo del feedback non può superare i 500 caratteri")
    @Schema(description = "Commento testuale opzionale sull'evento", example = "Evento organizzato molto bene, speaker molto preparati.", nullable = true)
    private String feedbackText;

    @NotNull(message = "L'ID dell'evento è obbligatorio")
    @Schema(description = "ID dell'evento a cui fa riferimento il feedback", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long eventId;
}