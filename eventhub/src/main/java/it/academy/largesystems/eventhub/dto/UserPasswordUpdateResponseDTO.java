package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "rappresentazione dei dati modificati e ritorno del nuovo token da usare")
public class UserPasswordUpdateResponseDTO {

    @Schema(description = "password precedente dell'utente")
    private String oldPassword;

    @Schema(description = "password nuova dell'utente")
    private String newPassword;

    @Schema(description = "token nuovo con credenziali con password modificata")
    private String token;
}
