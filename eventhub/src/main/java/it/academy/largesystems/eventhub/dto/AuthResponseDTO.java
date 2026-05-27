package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Payload restituito in caso di login effettuato con successo, contenente il token di autenticazione")
public class AuthResponseDTO {

    @Schema(description = "Il token JWT generato dal sistema da utilizzare per le richieste autenticate",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Il tipo di token utilizzato per l'autenticazione",
            example = "Bearer",
            allowableValues = {"Bearer"})
    private final String type = "Bearer";
}