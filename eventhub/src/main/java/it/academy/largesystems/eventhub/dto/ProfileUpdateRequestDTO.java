package it.academy.largesystems.eventhub.dto;

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
public class ProfileUpdateRequestDTO {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri")
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri")
    private String surname;

    @Past(message = "La data di nascita deve essere nel passato")
    private LocalDate birthDate;

    @Size(max = 100, message = "Il nome della città non può superare i 100 caratteri")
    private String city;

    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri")
    private String description;
}