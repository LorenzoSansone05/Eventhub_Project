package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.UserEmailUpdateRequestDTO;
import it.academy.largesystems.eventhub.dto.UserPasswordUpdateRequestDTO;
import it.academy.largesystems.eventhub.dto.UserResponseDTO;
import it.academy.largesystems.eventhub.dto.UserRoleUpdateRequestDTO;
import it.academy.largesystems.eventhub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ADMIN
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<String> updateEmail(@Valid @RequestBody UserEmailUpdateRequestDTO request) {
        userService.updateEmail(request.getNewEmail());
        return ResponseEntity.ok("Email aggiornata con successo.");
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UserPasswordUpdateRequestDTO request) {
        userService.updatePassword(request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password aggiornata con successo.");
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN
    @PatchMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequestDTO request) {

        userService.updateUserRole(id, request.getNewRole());
        return ResponseEntity.ok("Ruolo utente aggiornato con successo.");
    }
}