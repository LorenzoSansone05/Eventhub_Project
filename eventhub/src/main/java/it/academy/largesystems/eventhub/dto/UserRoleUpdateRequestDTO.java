package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Richiesta amministrativa di cambio ruolo per un utente")
public class UserRoleUpdateRequestDTO {

    @NotBlank(message = "Il nuovo ruolo è obbligatorio")
    @Schema(description = "Il nome del nuovo ruolo da assegnare", example = "MANAGER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newRole;
}