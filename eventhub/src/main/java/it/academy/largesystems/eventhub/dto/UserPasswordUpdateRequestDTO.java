package it.academy.largesystems.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdateRequestDTO {
    private String oldPassword;
    private String newPassword;
}
