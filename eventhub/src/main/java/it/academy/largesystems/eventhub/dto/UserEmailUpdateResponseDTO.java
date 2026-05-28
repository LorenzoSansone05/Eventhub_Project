package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "rappresentazione dei dati modificati e ritorno del nuovo token da usare")
public class UserEmailUpdateResponseDTO {

    @Schema(description = "vecchia email dell'utente")
    private String oldEmail;

    @Schema(description = "nuova email dell'utente")
    private String newEmail;

    @Schema(description = "token nuovo con email modificata")
    private String token;
}
