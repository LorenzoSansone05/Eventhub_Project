package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload per la richiesta di prenotazione di un biglietto")
public class BookTicketRequestDTO {

    @NotNull(message = "La tipologia di biglietto (STANDARD o VIP) è obbligatoria.")
    @Schema(description = "Tipologia di biglietto scelta", example = "STANDARD", allowableValues = {"STANDARD", "VIP"})
    private TicketType type;
}