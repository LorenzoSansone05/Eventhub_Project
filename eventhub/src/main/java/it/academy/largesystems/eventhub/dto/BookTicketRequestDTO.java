package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketRequestDTO {

    @NotNull(message = "L'ID dell'utente è obbligatorio per effettuare una prenotazione.")
    private Long userId;

    @NotNull(message = "La tipologia di biglietto (STANDARD o VIP) è obbligatoria.")
    private TicketType type;
}