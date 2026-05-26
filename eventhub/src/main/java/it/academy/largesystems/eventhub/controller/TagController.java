package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.TagRequestDTO;
import it.academy.largesystems.eventhub.dto.TagResponseDTO;
import it.academy.largesystems.eventhub.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // ADMIN
    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO request) {
        return new ResponseEntity<>(tagService.createTag(request), HttpStatus.CREATED);
    }

    // USER o ORGANIZER (review)
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDTO request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    // ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}