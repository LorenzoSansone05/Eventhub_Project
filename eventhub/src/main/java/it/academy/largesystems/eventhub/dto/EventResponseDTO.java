package it.academy.largesystems.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    // Event
    private Long id;
    private String name;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private double priceStandard;
    private double priceVip;

    // Venue
    private Long venueId;
    private String venueName;
    private String venueCity;
    private String venueAddress;

    // User
    private Long organizerId;
    private String organizerEmail;

    // Tags e Speakers
    private Set<String> tags;
    private List<String> speakerNames;

    // Feedback
    private List<String> feedbacks;
}
