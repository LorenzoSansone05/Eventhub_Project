package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
public class EventCreateRequestDTO {

    @NotBlank(message = "Il nome dell'evento è obbligatorio.")
    @Size(max = 150, message = "Il nome dell'evento non può superare i 150 caratteri.")
    private String name;

    @NotNull(message = "La data dell'evento è obbligatoria.")
    @FutureOrPresent(message = "La data dell'evento non può essere nel passato.")
    private LocalDate eventDate;

    @NotNull(message = "L'orario di inizio è obbligatorio.")
    private LocalTime startTime;

    @NotNull(message = "L'orario di fine è obbligatorio.")
    private LocalTime endTime;

    @PositiveOrZero(message = "Il prezzo Standard non può essere negativo.")
    private double priceStandard;

    @PositiveOrZero(message = "Il prezzo VIP non può essere negativo.")
    private double priceVip;

    @NotNull(message = "L'ID della struttura (venue) è obbligatorio.")
    private Long venueId;

    private Set<Long> tagIds;
    private List<Long> speakerIds;
}