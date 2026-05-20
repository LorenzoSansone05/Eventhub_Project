package it.academy.largesystems.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDTO {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String city;
    private String description;
}
