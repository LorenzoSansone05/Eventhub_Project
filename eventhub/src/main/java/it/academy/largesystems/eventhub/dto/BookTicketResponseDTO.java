package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rappresentazione del biglietto emesso dal sistema")
public class BookTicketResponseDTO {

    @Schema(description = "ID univoco del biglietto generato", example = "42")
    private Long id;

    @Schema(description = "ID dell'evento associato", example = "1")
    private Long eventId;

    @Schema(description = "Nome dell'evento", example = "Java Academy Conference 2026")
    private String eventName;

    @Schema(description = "Data dell'evento", example = "2026-09-20")
    private LocalDate eventDate;

    @Schema(description = "ID dell'utente che ha prenotato il biglietto", example = "9")
    private Long userId;

    @Schema(description = "Email dell'utente titolare del biglietto", example = "mario.rossi@example.com")
    private String userEmail;

    @Schema(description = "Tipologia del biglietto", example = "STANDARD")
    private TicketType type;

    @Schema(description = "Stato corrente del biglietto", example = "CONFIRMED")
    private TicketStatus status;

    @Schema(description = "Prezzo finale applicato al biglietto (in base alla tipologia scelta)", example = "49.99")
    private double price;
}