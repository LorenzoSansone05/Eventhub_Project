package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequestDTO {
    @NotBlank(message = "Il nome del tag è obbligatorio.")
    @Size(max = 50, message = "Il tag non può superare i 50 caratteri.")
    private String name;
}