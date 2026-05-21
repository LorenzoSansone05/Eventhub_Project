package it.academy.largesystems.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateRequestDTO {

    @NotBlank(message = "Il nuovo ruolo è obbligatorio")
    private String newRole;
}