package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.BookTicketRequestDTO;
import it.academy.largesystems.eventhub.entity.Ticket;
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
    public ResponseEntity<Ticket> createBooking(
            @PathVariable("id") Long eventId,
            @Valid @RequestBody BookTicketRequestDTO request) {

        Ticket newTicket = ticketService.createBooking(request.getUserId(), eventId, request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ticket> deleteBooking(@PathVariable("id") Long ticketId) {

        Ticket deleted = ticketService.deleteBooking(ticketId);
        return ResponseEntity.ok(deleted);
    }
}