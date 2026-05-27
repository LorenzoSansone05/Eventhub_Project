package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dettagli del profilo utente restituito dal sistema")
public class UserResponseDTO {

    @Schema(description = "ID univoco del record", example = "1")
    private Long id;

    @Schema(description = "Indirizzo email dell'utente", example = "utente@example.com")
    private String email;

    @Schema(description = "Nome del ruolo assegnato all'utente", example = "ADMIN")
    private String roleName;

    @Schema(description = "Stato di ban dell'utente", example = "false")
    private boolean isBanned;

    @Schema(description = "Lista dei testi di feedback lasciati dall'utente", example = "[\"Ottimo evento!\", \"Organizzazione da migliorare.\"]")
    private List<String> myFeedbacks;
}