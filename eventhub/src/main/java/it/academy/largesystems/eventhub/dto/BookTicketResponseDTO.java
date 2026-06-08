package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rappresentazione completa del biglietto emesso dal sistema per la gestione dell'utente e dell'amministratore")
public class BookTicketResponseDTO {

    @Schema(description = "ID univoco del biglietto generato dal database", example = "42")
    private Long id;

    @Schema(description = "ID dell'evento a cui il biglietto si riferisce", example = "1")
    private Long eventId;

    @Schema(description = "Nome o titolo ufficiale dell'evento", example = "Java Academy Conference 2026")
    private String eventName;

    @Schema(description = "Data in cui si svolgerà l'evento", example = "2026-09-20")
    private LocalDate eventDate;

    @Schema(description = "Orario ufficiale di inizio dell'evento", example = "21:00:00")
    private LocalTime startTime;

    @Schema(description = "Orario ufficiale di conclusione stimato dell'evento", example = "23:30:00")
    private LocalTime endTime;

    @Schema(description = "ID univoco dell'utente che ha acquistato o prenotato il biglietto", example = "9")
    private Long userId;

    @Schema(description = "Indirizzo email dell'utente titolare del biglietto", example = "mario.rossi@example.com")
    private String userEmail;

    @Schema(description = "Tipologia di biglietto selezionata (es. STANDARD, VIP)", example = "STANDARD")
    private TicketType type;

    @Schema(description = "Stato corrente del biglietto all'interno del sistema (es. PRENOTATO, ANNULLATO)", example = "PRENOTATO")
    private TicketStatus status;

    @Schema(description = "Prezzo finale applicato in base alla tipologia e ad eventuali sconti", example = "49.99")
    private double price;

    @Schema(description = "Flag booleano che indica se l'utente ha già rilasciato un feedback/recensione per questo evento specifico", example = "false")
    private boolean alreadyReviewed;
}