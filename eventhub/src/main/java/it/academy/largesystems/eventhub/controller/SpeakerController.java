package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.SpeakerRequestDTO;
import it.academy.largesystems.eventhub.dto.SpeakerResponseDTO;
import it.academy.largesystems.eventhub.service.SpeakerService;
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

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
@AllArgsConstructor
@Tag(name = "Speaker", description = "Specifiche per l'anagrafica e la gestione dei relatori degli eventi")
public class SpeakerController {

    private final SpeakerService speakerService;

    // ORGANIZER, ADMIN
    @GetMapping
    @Operation(summary = "Recupera tutti i relatori", description = "Restituisce la lista completa di tutti gli speaker censiti a sistema.")
    @ApiResponse(responseCode = "200", description = "Lista recuperata con successo")
    public ResponseEntity<List<SpeakerResponseDTO>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }

    // ADMIN
    @GetMapping("/{id}")
    @Operation(summary = "Recupera speaker per ID", description = "Restituisce i dettagli di un singolo relatore tramite il suo identificativo univoco.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatore trovato"),
            @ApiResponse(responseCode = "404", description = "Nessun relatore trovato con l'ID fornito")
    })
    public ResponseEntity<SpeakerResponseDTO> getSpeakerById(
            @PathVariable @Parameter(description = "ID univoco dello speaker", example = "12") Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }

    // ADMIN
    @PostMapping
    @Operation(summary = "Crea un nuovo relatore", description = "Registra un nuovo speaker nel database rendendolo disponibile per i futuri eventi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relatore creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di input non validi (es. campi obbligatori vuoti o bio troppo lunga)")
    })
    public ResponseEntity<SpeakerResponseDTO> createSpeaker(@Valid @RequestBody SpeakerRequestDTO speakerDTO) {
        SpeakerResponseDTO created = speakerService.createSpeaker(speakerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // ADMIN
    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna un relatore esistente", description = "Consente di modificare l'anagrafica o la biografia di uno speaker specifico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatore aggiornato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di modifica non validi"),
            @ApiResponse(responseCode = "404", description = "Relatore non trovato nel sistema")
    })
    public ResponseEntity<SpeakerResponseDTO> updateSpeaker(
            @PathVariable @Parameter(description = "ID dello speaker da modificare", example = "12") Long id,
            @Valid @RequestBody SpeakerRequestDTO speakerDetails) {
        SpeakerResponseDTO updated = speakerService.updateSpeaker(id, speakerDetails);
        return ResponseEntity.ok(updated);
    }

    // ADMIN
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un relatore", description = "Rimuove in modo permanente uno speaker dal sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relatore eliminato con successo"),
            @ApiResponse(responseCode = "404", description = "Nessun relatore associato a questo ID")
    })
    public ResponseEntity<Void> deleteSpeaker(
            @PathVariable @Parameter(description = "ID dello speaker da eliminare", example = "12") Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }
}