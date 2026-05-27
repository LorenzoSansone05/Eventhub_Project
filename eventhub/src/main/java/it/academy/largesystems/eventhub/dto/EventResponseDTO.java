package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rappresentazione dettagliata dell'evento restituita al client")
public class EventResponseDTO {

    @Schema(description = "ID univoco dell'evento recuperato dal database", example = "1")
    private Long id;

    @Schema(description = "Nome dell'evento", example = "Java Academy Conference 2026")
    private String name;

    @Schema(description = "Data dell'evento", example = "2026-09-20")
    private LocalDate eventDate;

    @Schema(description = "Orario di inizio", example = "09:30:00")
    private LocalTime startTime;

    @Schema(description = "Orario di fine", example = "18:00:00")
    private LocalTime endTime;

    @Schema(description = "Costo del biglietto standard", example = "49.99")
    private double priceStandard;

    @Schema(description = "Costo del biglietto VIP", example = "99.99")
    private double priceVip;

    @Schema(description = "ID della struttura ospitante", example = "4")
    private Long venueId;

    @Schema(description = "Nome della struttura ospitante", example = "Palapartenope")
    private String venueName;

    @Schema(description = "Città in cui si trova la struttura", example = "Napoli")
    private String venueCity;

    @Schema(description = "Indirizzo completo della struttura", example = "Via Coroglio, 14B")
    private String venueAddress;

    @Schema(description = "ID dell'organizzatore", example = "9")
    private Long organizerId;

    @Schema(description = "Email di contatto dell'organizzatore", example = "manager@eventhub.it")
    private String organizerEmail;

    @Schema(description = "Insieme dei nomi delle etichette descrittive", example = "[\"Programmazione\", \"Backend\"]")
    private Set<String> tags;

    @Schema(description = "Nomi completi dei relatori partecipanti", example = "[\"Mario Rossi\", \"Jane Doe\"]")
    private List<String> speakerNames;

    @Schema(description = "Storico dei feedback formattati comprensivi di voto, testo ed utente",
            example = "[\"5/5 - Ottima organizzazione! (user@test.it)\", \"4/5 (studente@test.it)\"]")
    private List<String> feedbacks;
}