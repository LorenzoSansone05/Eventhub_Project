package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rappresentazione compatta dell'evento per viste riassuntive e liste")
public class EventSummaryResponseDTO {

    @Schema(description = "ID univoco dell'evento", example = "1")
    private Long id;

    @Schema(description = "Nome dell'evento", example = "Java Academy Conference 2026")
    private String name;

    @Schema(description = "Data dell'evento", example = "2026-09-20")
    private LocalDate eventDate;

    @Schema(description = "Orario di inizio", example = "09:30:00")
    private LocalTime startTime;

    @Schema(description = "Costo a partire da", example = "49.99")
    private double priceStandard;

    @Schema(description = "Città in cui si svolge", example = "Napoli")
    private String venueCity;

    @Schema(description = "Insieme dei nomi delle etichette", example = "[\"Programmazione\"]")
    private Set<String> tags;
}
