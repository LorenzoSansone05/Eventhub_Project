package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.ProfileUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.Profile;
import it.academy.largesystems.eventhub.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}/me")
    public ResponseEntity<Profile> getMyProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}/me")
    public ResponseEntity<Profile> updateMyProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileUpdateRequestDTO request) {

        Profile updatedProfile = profileService.updateProfileByUserId(userId, request);
        return ResponseEntity.ok(updatedProfile);
    }
}
