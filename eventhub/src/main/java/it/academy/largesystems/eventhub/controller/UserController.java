package it.academy.largesystems.eventhub.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import it.academy.largesystems.eventhub.dto.*;
import it.academy.largesystems.eventhub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API per la gestione degli utenti (Funzionalità Admin e Profilo)")
public class UserController {

    private final UserService userService;

    // ADMIN
    @GetMapping
    @Operation(summary = "Recupera tutti gli utenti", description = "Restituisce la lista completa di tutti gli utenti registrati. Richiede privilegi di ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista recuperata con successo")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ADMIN
    @GetMapping("/{id}")
    @Operation(summary = "Recupera un utente tramite ID", description = "Restituisce i dettagli di un singolo utente tramite il suo ID univoco. Richiede privilegi di ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente trovato con successo"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID dell'utente da cercare", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ADMIN
    @GetMapping("/by-email/{email}")
    @Operation(
            summary = "Recupera un utente tramite email",
            description = "Restituisce i dettagli del profilo di un utente cercandolo attraverso il suo indirizzo email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente trovato con successo"),
            @ApiResponse(responseCode = "400", description = "Formato email non valido (es. manca la @ o il dominio)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nessun utente trovato con l'indirizzo email fornito", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @PathVariable
            @Parameter(description = "L'indirizzo email dell'utente da cercare", example = "utente@example.com")
            String email) {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PatchMapping("/me/email")
    @Operation(summary = "Aggiorna l'email dell'utente corrente", description = "Permette all'utente loggato di modificare il proprio indirizzo email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email aggiornata con successo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEmailUpdateResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dati di input non validi", content = @Content)
    })
    public ResponseEntity<UserEmailUpdateResponseDTO> updateEmail(@Valid @RequestBody UserEmailUpdateRequestDTO request) {
        return ResponseEntity.ok().body(userService.updateEmail(request.getNewEmail()));
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Aggiorna la password dell'utente corrente", description = "Permette all'utente loggato di modificare la propria password di accesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password aggiornata con successo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPasswordUpdateResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Vecchia password errata o nuova password non valida", content = @Content)
    })
    public ResponseEntity<UserPasswordUpdateResponseDTO> updatePassword(@Valid @RequestBody UserPasswordUpdateRequestDTO request) {
        return ResponseEntity.ok().body(userService.updatePassword(request.getOldPassword(), request.getNewPassword()));
    }

    // ADMIN
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un utente", description = "Rimuove permanentemente un utente dal sistema. Richiede privilegi di ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utente eliminato con successo"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID dell'utente da eliminare", example = "1") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN
    @PatchMapping("/{id}/role")
    @Operation(summary = "Aggiorna il ruolo di un utente", description = "Permette di modificare il ruolo di un utente specifico. Richiede privilegi di ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruolo aggiornato con successo", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Ruolo non valido"),
            @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<String> updateUserRole(
            @Parameter(description = "ID dell'utente a cui modificare il ruolo", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequestDTO request) {

        userService.updateUserRole(id, request.getNewRole());
        return ResponseEntity.ok("Ruolo utente aggiornato con successo.");
    }

    // ADMIN
    @PatchMapping("/{id}/ban")
    @Operation(summary = "Aggiorna lo stato di ban di un utente", description = "Permette di bannare o sbannare un utente specifico tramite query param 'banned'. Richiede privilegi di ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stato di ban aggiornato con successo", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Utente non trovato"),
            @ApiResponse(responseCode = "409", description = "Conflitto: l'amministratore ha provato a bannare se stesso")
    })
    public ResponseEntity<String> updateUserBanStatus(
            @Parameter(description = "ID dell'utente a cui modificare lo stato di ban", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuovo stato di ban (true per bannare, false per sbloccare)", example = "true") @RequestParam boolean banned) {

        userService.updateUserBanStatus(id, banned);

        String messaggio = banned ? "Utente bannato con successo." : "Utente sbloccato con successo.";
        return ResponseEntity.ok(messaggio);
    }
}