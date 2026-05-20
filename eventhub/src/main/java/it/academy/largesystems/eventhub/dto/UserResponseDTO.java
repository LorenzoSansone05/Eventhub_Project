package it.academy.largesystems.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isBanned;
    private String roleName;
}
