package it.academy.largesystems.eventhub.dto;

import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketResponseDTO {
    private Long id;
    private Long eventId;
    private String eventName;
    private LocalDate eventDate;
    private Long userId;
    private String userEmail;
    private TicketType type;
    private TicketStatus status;
    private double price;
}