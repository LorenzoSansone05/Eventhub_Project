package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.SpeakerRequestDTO;
import it.academy.largesystems.eventhub.dto.SpeakerResponseDTO;
import it.academy.largesystems.eventhub.service.SpeakerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
@AllArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;

    @GetMapping
    public ResponseEntity<List<SpeakerResponseDTO>> getAllSpeakers() {
        return ResponseEntity.ok(speakerService.getAllSpeakers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeakerResponseDTO> getSpeakerById(@PathVariable Long id) {
        return ResponseEntity.ok(speakerService.getSpeakerById(id));
    }

    @PostMapping
    public ResponseEntity<SpeakerResponseDTO> createSpeaker(@Valid @RequestBody SpeakerRequestDTO speakerDTO) {
        SpeakerResponseDTO created = speakerService.createSpeaker(speakerDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpeakerResponseDTO> updateSpeaker(@PathVariable Long id, @Valid @RequestBody SpeakerRequestDTO speakerDetails) {
        SpeakerResponseDTO updated = speakerService.updateSpeaker(id, speakerDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }
}