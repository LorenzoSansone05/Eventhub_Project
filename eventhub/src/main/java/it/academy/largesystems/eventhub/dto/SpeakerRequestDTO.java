package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SpeakerRequestDTO {

    @NotBlank(message = "Il nome del relatore è obbligatorio.")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri.")
    private String name;

    @NotBlank(message = "Il cognome del relatore è obbligatorio.")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri.")
    private String surname;

    @Size(max = 1000, message = "La biografia non può superare i 1000 caratteri.")
    private String bio;
}