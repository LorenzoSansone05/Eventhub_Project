package it.academy.largesystems.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParticipantResponseDTO {
    private Long ticketId;
    private Long userId;
    private String email;
    private String ticketType;
    private String ticketStatus;
}
