package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.ProfileResponseDTO;
import it.academy.largesystems.eventhub.dto.ProfileUpdateRequestDTO;
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

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDTO> getMyProfile() {
        ProfileResponseDTO profile = profileService.getProfileByUserId();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponseDTO> updateMyProfile(@Valid @RequestBody ProfileUpdateRequestDTO request) {
        ProfileResponseDTO updatedProfile = profileService.updateProfileByUserId(request);
        return ResponseEntity.ok(updatedProfile);
    }
}