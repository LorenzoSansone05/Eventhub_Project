package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.BookTicketRequestDTO;
import it.academy.largesystems.eventhub.dto.BookTicketResponseDTO;
import it.academy.largesystems.eventhub.service.TicketService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;


    @PostMapping("/events/{id}/book")
    public ResponseEntity<BookTicketResponseDTO> createBooking(
            @PathVariable("id") Long eventId,
            @Valid @RequestBody BookTicketRequestDTO request) {

        BookTicketResponseDTO newTicket = ticketService.createBooking(eventId, request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long ticketId) {
        ticketService.deleteBooking(ticketId);
        return ResponseEntity.noContent().build();
    }
}