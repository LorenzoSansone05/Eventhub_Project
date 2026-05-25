package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.entity.Speaker;
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
    public ResponseEntity<List<Speaker>> getAllSpeakers() {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        return ResponseEntity.ok(speakers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Speaker> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerService.getSpeakerById(id);
        return ResponseEntity.ok(speaker);
    }

    @PostMapping
    public ResponseEntity<Speaker> createSpeaker(@Valid @RequestBody Speaker speaker) {
        Speaker createdSpeaker = speakerService.createSpeaker(speaker);
        return new ResponseEntity<>(createdSpeaker, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Speaker> updateSpeaker(@PathVariable Long id, @Valid @RequestBody Speaker speakerDetails) {
        Speaker updatedSpeaker = speakerService.updateSpeaker(id, speakerDetails);
        return ResponseEntity.ok(updatedSpeaker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.noContent().build();
    }
}