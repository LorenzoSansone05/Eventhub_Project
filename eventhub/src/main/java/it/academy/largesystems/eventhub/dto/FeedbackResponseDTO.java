package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object che rappresenta la risposta di un feedback per il Front-End")
public class FeedbackResponseDTO {

    @Schema(description = "ID univoco del feedback", example = "1")
    private Long id;

    @Schema(description = "Punteggio della recensione (da 1 a 5)", example = "5")
    private int rating;

    @Schema(description = "Testo del feedback lasciato dall'utente", example = "Evento fantastico, organizzazione impeccabile!")
    private String feedbackText;

    @Schema(description = "ID dell'utente che ha scritto il feedback", example = "42")
    private Long userId;

    @Schema(description = "Username dell'utente che ha scritto il feedback", example = "mario_rossi")
    private String username;

    @Schema(description = "ID dell'evento a cui si riferisce il feedback", example = "109")
    private Long eventId;

    @Schema(description = "Titolo dell'evento a cui si riferisce il feedback", example = "Conferenza Spring Boot 2026")
    private String eventName;
}
