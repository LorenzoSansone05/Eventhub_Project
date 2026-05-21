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
public class VenueRequestDTO {

    @NotBlank(message = "Il nome del locale è obbligatorio")
    @Size(min = 2, max = 100, message = "Il nome deve avere tra 2 e 100 caratteri")
    private String name;

    @Min(value = 10, message = "La capacità deve essere di almeno 10 persone")
    private int capacity;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    private String address;

    @NotBlank(message = "La città è obbligatoria")
    private String city;
}
