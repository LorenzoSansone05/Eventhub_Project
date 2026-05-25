package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketRequestDTO {
    private Long userId; // L'utente che sta effettuando la prenotazione
    private TicketType type; // STANDARD o VIP
}
