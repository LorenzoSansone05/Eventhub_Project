package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dettagli della location restituiti dal sistema")
public class VenueResponseDTO {

    @Schema(description = "ID univoco della location", example = "1")
    private Long id;

    @Schema(description = "Nome della location", example = "Alcatraz")
    private String name;

    @Schema(description = "Capacità massima della struttura", example = "3000")
    private int capacity;

    @Schema(description = "Indirizzo della location", example = "Via Valtellina 25")
    private String address;

    @Schema(description = "Città di ubicazione", example = "Milano")
    private String city;
}