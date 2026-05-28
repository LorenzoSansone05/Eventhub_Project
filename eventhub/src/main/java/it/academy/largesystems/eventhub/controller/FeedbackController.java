package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.FeedbackRequestDTO;
import it.academy.largesystems.eventhub.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Specifiche per l'inserimento, la modifica e la cancellazione delle recensioni degli eventi")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // USER
    @PostMapping
    @Operation(
            summary = "Inserisce un nuovo feedback (Richiede ruolo USER)",
            description = "Consente a un utente autenticato di lasciare una recensione (voto e commento opzionale) per un determinato evento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback inserito con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi (voto fuori dai limiti 1-5 o testo troppo lungo)"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "404", description = "Evento di riferimento non trovato")
    })
    public ResponseEntity<String> create(@Valid @RequestBody FeedbackRequestDTO request) {
        feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Feedback inserito con successo.");
    }

    // ADMIN
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifica un feedback esistente (Richiede ruolo ADMIN)",
            description = "Consente a un amministratore di modificare i dati di un feedback specifico tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback aggiornato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di modifica non validi"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti (l'utente non è un ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Feedback o evento correlato non trovato")
    })
    public ResponseEntity<String> update(
            @PathVariable @Parameter(description = "ID univoco del feedback da modificare", example = "10") Long id,
            @Valid @RequestBody FeedbackRequestDTO request) {
        feedbackService.updateFeedback(id, request);
        return ResponseEntity.ok("Feedback aggiornato con successo.");
    }

    // ADMIN
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un feedback (Richiede ruolo ADMIN)",
            description = "Consente a un amministratore di rimuovere definitivamente un feedback dal sistema tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback eliminato con successo"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti (l'utente non è un ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Feedback non trovato")
    })
    public ResponseEntity<String> delete(
            @PathVariable @Parameter(description = "ID del feedback da eliminare", example = "10") Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback eliminato con successo.");
    }
}