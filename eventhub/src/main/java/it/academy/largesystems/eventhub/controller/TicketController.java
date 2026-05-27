package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.BookTicketRequestDTO;
import it.academy.largesystems.eventhub.dto.BookTicketResponseDTO;
import it.academy.largesystems.eventhub.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
@Tag(name = "Biglietti", description = "Specifiche per l'acquisto, la prenotazione e la cancellazione dei biglietti per gli eventi")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/events/{id}/book")
    @Operation(
            summary = "Prenota un biglietto per un evento (Richiede ruolo USER)",
            description = "Consente all'utente autenticato di prenotare un biglietto (STANDARD o VIP) per l'evento specificato nell'URL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Biglietto prenotato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi o tipologia biglietto mancante"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "404", description = "Evento specificato non trovato nel sistema")
    })
    public ResponseEntity<BookTicketResponseDTO> createBooking(
            @PathVariable("id") @Parameter(description = "ID dell'evento per cui prenotare il biglietto", example = "1") Long eventId,
            @Valid @RequestBody BookTicketRequestDTO request) {

        BookTicketResponseDTO newTicket = ticketService.createBooking(eventId, request.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disdici una prenotazione / Elimina biglietto",
            description = "Consente di cancellare una prenotazione esistente tramite l'ID del biglietto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prenotazione cancellata con successo"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti per cancellare questa prenotazione"),
            @ApiResponse(responseCode = "404", description = "Biglietto non trovato")
    })
    public ResponseEntity<Void> deleteBooking(
            @PathVariable("id") @Parameter(description = "ID univoco del biglietto da cancellare", example = "42") Long ticketId) {
        ticketService.deleteBooking(ticketId);
        return ResponseEntity.noContent().build();
    }
}