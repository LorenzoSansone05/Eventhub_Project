package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Rappresentazione dei dati del profilo utente restituita dal server")
public class ProfileResponseDTO {

    @Schema(description = "ID univoco del profilo memorizzato nel database", example = "15")
    private Long id;

    @Schema(description = "ID univoco dell'utente associato a questo profilo", example = "9")
    private Long userId;

    @Schema(description = "Indirizzo email dell'utente (recuperato dall'account)", example = "mario.rossi@example.com")
    private String email;

    @Schema(description = "Nome dell'utente", example = "Mario")
    private String name;

    @Schema(description = "Cognome dell'utente", example = "Rossi")
    private String surname;

    @Schema(description = "Foto dell'utente", example = "foto.png")
    private String photoUrl;

    @Schema(description = "Data di nascita dell'utente (formato ISO YYYY-MM-DD)", example = "1995-04-23")
    private LocalDate birthDate;

    @Schema(description = "Città di residenza inserita dall'utente", example = "Napoli", nullable = true)
    private String city;

    @Schema(description = "Breve biografia o presentazione inserita dall'utente",
            example = "Sviluppatore software appassionato di eventi tech.", nullable = true)
    private String description;
}