package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload per l'aggiornamento dei dati del profilo utente")
public class ProfileUpdateRequestDTO {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri")
    @Schema(description = "Nome dell'utente", example = "Mario", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri")
    @Schema(description = "Cognome dell'utente", example = "Rossi", requiredMode = Schema.RequiredMode.REQUIRED)
    private String surname;

    @NotBlank(message = "La Foto è obbligatoria")
    @Schema(description = "Foto dell'utente", example = "foto.png", requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoUrl;

    @Past(message = "La data di nascita deve essere nel passato")
    @Schema(description = "Data di nascita (deve essere una data passata)", example = "1995-04-23")
    private LocalDate birthDate;

    @Size(max = 100, message = "Il nome della città non può superare i 100 caratteri")
    @Schema(description = "Città di residenza o domicilio", example = "Napoli", nullable = true)
    private String city;

    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri")
    @Schema(description = "Breve biografia o presentazione dell'utente", example = "Sviluppatore software appassionato di eventi tech.", nullable = true)
    private String description;
}