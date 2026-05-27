package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dati richiesti per la creazione o l'aggiornamento di una location")
public class VenueRequestDTO {

    @NotBlank(message = "Il nome del locale è obbligatorio")
    @Size(min = 2, max = 100, message = "Il nome deve avere tra 2 e 100 caratteri")
    @Schema(description = "Nome della location", example = "Alcatraz", minLength = 2, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Min(value = 10, message = "La capacità deve essere di almeno 10 persone")
    @Schema(description = "Capacità massima di persone ospitabili (minimo 10)", example = "3000", minimum = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int capacity;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Schema(description = "Indirizzo stradale della location", example = "Via Valtellina 25", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotBlank(message = "La città è obbligatoria")
    @Schema(description = "Città in cui si trova la location", example = "Milano", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
}