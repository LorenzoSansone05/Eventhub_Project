package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.FeedbackRequestDTO;
import it.academy.largesystems.eventhub.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // USER
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody FeedbackRequestDTO request) {
        feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Feedback inserito con successo.");
    }

    // ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody FeedbackRequestDTO request) {
        feedbackService.updateFeedback(id, request);
        return ResponseEntity.ok("Feedback aggiornato con successo.");
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback eliminato con successo.");
    }
}
