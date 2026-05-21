package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdateRequestDTO {

    @NotBlank(message = "La vecchia password è obbligatoria")
    private String oldPassword;

    @NotBlank(message = "La nuova password è obbligatoria")
    @Size(min = 8, max = 100, message = "La nuova password deve essere di almeno 8 caratteri")
    private String newPassword;
}