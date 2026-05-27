package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.TagRequestDTO;
import it.academy.largesystems.eventhub.dto.TagResponseDTO;
import it.academy.largesystems.eventhub.service.TagService;
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

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "Specifiche per la catalogazione e la gestione delle etichette (tag) degli eventi")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(
            summary = "Crea un nuovo tag (Richiede ruolo ADMIN)",
            description = "Consente a un amministratore di inserire un nuovo tag a sistema per la categorizzazione degli eventi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag creato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di input non validi (es. nome vuoto o superiore a 50 caratteri)"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti per eseguire l'operazione")
    })
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO request) {
        return new ResponseEntity<>(tagService.createTag(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Recupera tutti i tag disponibili (Accessibile a USER e ORGANIZER)",
            description = "Restituisce l'elenco completo dei tag censiti a sistema per l'applicazione dei filtri di ricerca."
    )
    @ApiResponse(responseCode = "200", description = "Elenco dei tag recuperato con successo")
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Aggiorna un tag esistente (Richiede ruolo ADMIN)",
            description = "Consente a un amministratore di modificare il nome di un tag specifico tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag aggiornato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati di modifica non validi"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti per l'utente corrente"),
            @ApiResponse(responseCode = "404", description = "Nessun tag trovato con l'ID specificato")
    })
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable @Parameter(description = "ID univoco del tag da modificare", example = "3") Long id,
            @Valid @RequestBody TagRequestDTO request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un tag (Richiede ruolo ADMIN)",
            description = "Consente a un amministratore di rimuovere definitivamente un tag dal sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenuto vuoto. Tag eliminato con successo"),
            @ApiResponse(responseCode = "403", description = "Privilegi insufficienti per l'utente corrente"),
            @ApiResponse(responseCode = "404", description = "Tag non trovato")
    })
    public ResponseEntity<Void> deleteTag(
            @PathVariable @Parameter(description = "ID del tag da eliminare", example = "3") Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}