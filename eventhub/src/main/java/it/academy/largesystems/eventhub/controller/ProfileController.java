package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.ProfileResponseDTO;
import it.academy.largesystems.eventhub.dto.ProfileUpdateRequestDTO;
import it.academy.largesystems.eventhub.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Profilo", description = "Specifiche per la visualizzazione e la modifica del profilo dell'utente autenticato")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    @Operation(
            summary = "Recupera il profilo personale",
            description = "Restituisce le informazioni del profilo dell'utente attualmente autenticato nel sistema, ricavato dal contesto di sicurezza."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profilo recuperato con successo"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato / Token non valido"),
            @ApiResponse(responseCode = "404", description = "Dati del profilo non trovati per l'utente corrente")
    })
    public ResponseEntity<ProfileResponseDTO> getMyProfile() {
        ProfileResponseDTO profile = profileService.getProfileByUserId();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @Operation(
            summary = "Aggiorna il profilo personale",
            description = "Consente di modificare i dati anagrafici e descrittivi del proprio profilo. Tutti i campi obbligatori devono superare le validazioni."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profilo aggiornato con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi (es. data di nascita nel futuro o testi troppo lunghi)"),
            @ApiResponse(responseCode = "401", description = "Utente non autenticato"),
            @ApiResponse(responseCode = "404", description = "Profilo inesistente")
    })
    public ResponseEntity<ProfileResponseDTO> updateMyProfile(@Valid @RequestBody ProfileUpdateRequestDTO request) {
        ProfileResponseDTO updatedProfile = profileService.updateProfileByUserId(request);
        return ResponseEntity.ok(updatedProfile);
    }
}