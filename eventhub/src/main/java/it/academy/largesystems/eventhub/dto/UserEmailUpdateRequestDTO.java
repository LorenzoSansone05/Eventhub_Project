package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Richiesta per l'aggiornamento dell'email dell'utente")
public class UserEmailUpdateRequestDTO {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci un indirizzo email valido")
    @Schema(description = "Nuovo indirizzo email da associare all'account", example = "nuovo.utente@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newEmail;
}