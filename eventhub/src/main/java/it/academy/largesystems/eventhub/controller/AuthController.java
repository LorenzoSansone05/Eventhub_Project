package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.AuthRequestDTO;
import it.academy.largesystems.eventhub.dto.AuthResponseDTO;
import it.academy.largesystems.eventhub.service.LoginService;
import it.academy.largesystems.eventhub.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticazione", description = "Documentazione dei processi di registrazione e login")
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

    public AuthController(SignupService signupService, LoginService loginService) {
        this.signupService = signupService;
        this.loginService = loginService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Registra un nuovo utente", description = "Crea un account utente standard (ROLE_USER).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registrazione completata con successo"),
            @ApiResponse(responseCode = "400", description = "Dati non validi (email malformata o password < 6 caratteri)"),
            @ApiResponse(responseCode = "409", description = "email già presente nel sistema"),
            @ApiResponse(responseCode = "404", description = "Ruolo ROLE_USER non trovato")
    })
    public ResponseEntity<String> signup(@Valid @RequestBody AuthRequestDTO req) {
        signupService.signup(req.getEmail(), req.getPassword());
        return ResponseEntity.ok("Registrazione completata");
    }

    @PostMapping("/login")
    @Operation(summary = "Effettua il login", description = "Verifica le credenziali")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticazione riuscita"),
            @ApiResponse(responseCode = "400", description = "Formato dati di input non valido"),
            @ApiResponse(responseCode = "401", description = "Autenticazione fallita")
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO req) {
        String token = loginService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}