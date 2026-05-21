package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.VenueRequestDTO;
import it.academy.largesystems.eventhub.dto.VenueUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.Venue;
import it.academy.largesystems.eventhub.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<Venue> createVenue(@Valid @RequestBody VenueRequestDTO venue) {
        return new ResponseEntity<>(venueService.createVenue(venue), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueUpdateRequestDTO venueDetails) {
        return ResponseEntity.ok(venueService.updateVenue(id, venueDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build(); // Ritorna 204 No Content in caso di successo
    }
}