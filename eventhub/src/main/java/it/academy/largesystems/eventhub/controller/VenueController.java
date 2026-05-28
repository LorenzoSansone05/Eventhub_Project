package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.VenueRequestDTO;
import it.academy.largesystems.eventhub.dto.VenueResponseDTO;
import it.academy.largesystems.eventhub.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venue Management", description = "API per la gestione delle location (es. stadi, teatri, locali)")
public class VenueController {

    private final VenueService venueService;

    // ADMIN
    @PostMapping
    @Operation(summary = "Crea una nuova location", description = "Registra una nuova location nel sistema con i dettagli relativi a capacità e indirizzo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location creata con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di input non validi", content = @Content)
    })
    public ResponseEntity<VenueResponseDTO> createVenue(@Valid @RequestBody VenueRequestDTO venue) {
        return new ResponseEntity<>(venueService.createVenue(venue), HttpStatus.CREATED);
    }

    // ORGANIZER, ADMIN
    @GetMapping
    @Operation(summary = "Recupera tutte le location", description = "Restituisce la lista completa di tutte le location registrate.")
    @ApiResponse(responseCode = "200", description = "Lista delle location recuperata con successo")
    public ResponseEntity<List<VenueResponseDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    // ADMIN
    @GetMapping("/{id}")
    @Operation(summary = "Recupera una location tramite ID", description = "Restituisce i dettagli di una singola location specificando il suo ID univoco.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location trovata con successo"),
            @ApiResponse(responseCode = "404", description = "Location non trovata", content = @Content)
    })
    public ResponseEntity<VenueResponseDTO> getVenueById(
            @Parameter(description = "ID univoco della location da cercare", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    // ADMIN
    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna una location esistente", description = "Modifica integralmente i dati di una location esistente tramite il suo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location aggiornata con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di input non validi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Location non trovata", content = @Content)
    })
    public ResponseEntity<VenueResponseDTO> updateVenue(
            @Parameter(description = "ID della location da aggiornare", example = "1") @PathVariable Long id,
            @Valid @RequestBody VenueRequestDTO venueDetails) {
        return ResponseEntity.ok(venueService.updateVenue(id, venueDetails));
    }

    // ADMIN
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una location", description = "Rimuove permanentemente una location dal sistema tramite il suo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Location eliminata con successo"),
            @ApiResponse(responseCode = "404", description = "Location non trovata")
    })
    public ResponseEntity<Void> deleteVenue(
            @Parameter(description = "ID della location da eliminare", example = "1") @PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}