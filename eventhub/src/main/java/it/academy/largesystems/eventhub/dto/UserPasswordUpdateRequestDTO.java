package it.academy.largesystems.eventhub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Richiesta per il cambio password")
public class UserPasswordUpdateRequestDTO {

    @NotBlank(message = "La vecchia password è obbligatoria")
    @Schema(description = "La password attualmente attiva", example = "VecchiaPassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @NotBlank(message = "La nuova password è obbligatoria")
    @Size(min = 8, max = 100, message = "La nuova password deve essere di almeno 8 caratteri")
    @Schema(description = "La nuova password da impostare (min 8, max 100 caratteri)", example = "NuovaPassword456!", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}