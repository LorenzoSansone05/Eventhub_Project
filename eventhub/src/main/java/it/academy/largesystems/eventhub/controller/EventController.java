package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.EventCreateRequestDTO;
import it.academy.largesystems.eventhub.dto.EventResponseDTO;
import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.service.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getEventsByFilters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String venueName,
            @RequestParam(required = false) String organizerName,
            Pageable pageable) {

        Page<EventResponseDTO> eventsByFilters = eventService.getEventsByFilters(date, tag, venueName, organizerName, pageable);
        return ResponseEntity.ok(eventsByFilters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateRequestDTO event) {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventCreateRequestDTO eventDetails) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<String> getEventRating(@PathVariable Long id) {
        String ratingSummary = eventService.getEventRatingSummary(id);
        return ResponseEntity.ok(ratingSummary);
    }
}