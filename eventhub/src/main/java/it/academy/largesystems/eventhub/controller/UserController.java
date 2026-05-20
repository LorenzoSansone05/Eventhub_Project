package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.UserEmailUpdateRequestDTO;
import it.academy.largesystems.eventhub.dto.UserPasswordUpdateRequestDTO;
import it.academy.largesystems.eventhub.dto.UserRoleUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<String> updateEmail(
            @PathVariable Long id,
            @RequestBody UserEmailUpdateRequestDTO request) {
        userService.updateEmail(id, request.getNewEmail());
        return ResponseEntity.ok("Email aggiornata con successo.");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long id,
            @RequestBody UserPasswordUpdateRequestDTO request) {
        userService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password aggiornata con successo.");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestBody UserRoleUpdateRequestDTO request) {

        userService.updateUserRole(id, request.getNewRole());
        return ResponseEntity.ok("Ruolo utente aggiornato con successo.");
    }
}
