package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.EventCreateRequestDTO;
import it.academy.largesystems.eventhub.dto.EventResponseDTO;
import it.academy.largesystems.eventhub.dto.EventSummaryResponseDTO;
import it.academy.largesystems.eventhub.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Tag(name = "Eventi", description = "Specifiche per la ricerca, creazione, modifica e rimozione degli eventi")
public class EventController {

    private final EventService eventService;

    // TUTTI
    @GetMapping
    @Operation(summary = "Recupera eventi filtrati e paginati", description = "Restituisce una pagina di eventi in base ai filtri applicati.")
    @ApiResponse(responseCode = "200", description = "Elenco recuperato con successo")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByFilters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String venueName,
            @RequestParam(required = false) String organizerName,
            Pageable pageable) {
        Page<EventResponseDTO> eventsByFilters = eventService.getEventsByFilters(date, tag, venueName, organizerName, pageable);
        return ResponseEntity.ok(eventsByFilters);
    }

    // ADMIN
    @GetMapping("/{id}")
    @Operation(summary = "Recupera un evento per ID", description = "Mostra i dettagli completi dell'evento, inclusi i feedback aggregati.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento trovato"),
            @ApiResponse(responseCode = "404", description = "Nessun evento associato a questo ID")
    })
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // ORGANIZER
    @PostMapping
    @Operation(summary = "Crea un nuovo evento (Richiede ruolo ORGANIZER)", description = "Salva un nuovo evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creato correttamente"),
            @ApiResponse(responseCode = "400", description = "Prezzo standard superiore al VIP o vincoli violati"),
            @ApiResponse(responseCode = "403", description = "L'utente non ha i privilegi di organizzatore o è bannato"),
            @ApiResponse(responseCode = "404", description = "La struttura o un relatore non esistono")
    })
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateRequestDTO event) {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.CREATED);
    }

    // ORGANIZER
    @PutMapping("/{id}")
    @Operation(summary = "Modifica un evento esistente (Richiede ruolo ORGANIZER)", description = "Aggiorna i dati dell'evento identificato dall'ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento aggiornato"),
            @ApiResponse(responseCode = "400", description = "Dati non validi"),
            @ApiResponse(responseCode = "403", description = "L'utente corrente non coincide con l'organizzatore dell'evento"),
            @ApiResponse(responseCode = "404", description = "Evento, struttura o speaker non trovati")
    })
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventCreateRequestDTO eventDetails) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails));
    }

    // ORGANIZER
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un evento (Richiede ruolo ORGANIZER)", description = "Rimuove l'evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento eliminato con successo"),
            @ApiResponse(responseCode = "403", description = "Permessi insufficienti per la cancellazione dell'evento"),
            @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // TUTTI
    @GetMapping("/{id}/rating")
    @Operation(summary = "Recupera la media dei feedback dell'evento", description = "Restituisce un riepilogo stringa con la media matematica delle recensioni lasciate dagli utenti.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Riepilogo generato"),
            @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<String> getEventRating(@PathVariable Long id) {
        String ratingSummary = eventService.getEventRatingSummary(id);
        return ResponseEntity.ok(ratingSummary);
    }
}