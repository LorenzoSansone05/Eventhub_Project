package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Schema(description = "Payload richiesto per la creazione o modifica di un evento")
public class EventCreateRequestDTO {

    @NotBlank(message = "Il nome dell'evento è obbligatorio.")
    @Size(max = 150, message = "Il nome dell'evento non può superare i 150 caratteri.")
    @Schema(description = "Nome dell'evento", example = "Java Academy Conference 2026", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "La data dell'evento è obbligatoria.")
    @FutureOrPresent(message = "La data dell'evento non può essere nel passato.")
    @Schema(description = "Data pianificata per l'evento (Oggi o futura)", example = "2026-09-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate eventDate;

    @NotNull(message = "L'orario di inizio è obbligatorio.")
    @Schema(description = "Orario di inizio dell'evento", example = "09:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime startTime;

    @NotNull(message = "L'orario di fine è obbligatorio.")
    @Schema(description = "Orario stimato di conclusione", example = "18:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime endTime;

    @PositiveOrZero(message = "Il prezzo Standard non può essere negativo.")
    @Schema(description = "Prezzo del biglietto standard (deve essere inferiore o uguale al prezzo VIP)", example = "49.99")
    private double priceStandard;

    @PositiveOrZero(message = "Il prezzo VIP non può essere negativo.")
    @Schema(description = "Prezzo del biglietto VIP", example = "99.99")
    private double priceVip;

    @NotNull(message = "L'ID della struttura (venue) è obbligatorio.")
    @Schema(description = "ID identificativo della struttura dove si terrà l'evento", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long venueId;

    @Schema(description = "Insieme di ID dei tag associati", example = "[1, 3, 5]")
    private Set<Long> tagIds;

    @Schema(description = "Lista di ID dei relatori (speaker) invitati", example = "[12, 15]")
    private List<Long> speakerIds;
}