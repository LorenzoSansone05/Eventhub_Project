package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailUpdateRequestDTO {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci un indirizzo email valido")
    private String newEmail;
}