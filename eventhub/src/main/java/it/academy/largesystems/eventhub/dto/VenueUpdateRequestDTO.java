package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueUpdateRequestDTO {

    @NotBlank(message = "Il nome del locale è obbligatorio per l'aggiornamento")
    @Size(min = 2, max = 100, message = "Il nome deve avere tra 2 e 100 caratteri")
    private String name;

    @Min(value = 1, message = "La capacità deve essere di almeno 1 persona")
    private int capacity;

    @NotBlank(message = "L'indirizzo è obbligatorio per l'aggiornamento")
    private String address;

    @NotBlank(message = "La città è obbligatoria per l'aggiornamento")
    private String city;
}
