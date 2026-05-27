package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modello per le richieste di autenticazione (Login/Signup)")
public class AuthRequestDTO {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci un email valida")
    @Schema(description = "Indirizzo email dell'utente", example = "utente@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve contenere almeno 6 caratteri.")
    @Schema(description = "Password dell'utente (minimo 6 caratteri)", example = "Secret123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}