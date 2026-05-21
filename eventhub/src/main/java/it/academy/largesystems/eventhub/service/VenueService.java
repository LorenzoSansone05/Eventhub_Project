package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.dto.VenueRequestDTO;
import it.academy.largesystems.eventhub.dto.VenueUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.Venue;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    public Venue createVenue(VenueRequestDTO venueRequest) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getName());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setAddress(venueRequest.getAddress());
        venue.setCity(venueRequest.getCity());

        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id: " + id));
    }

    public Venue updateVenue(Long id, VenueUpdateRequestDTO venueDetails) {
        Venue existingVenue = getVenueById(id); // Richiamo il metodo che effettua la query in base all'id per non scrivetre codice duplicato

        existingVenue.setName(venueDetails.getName());
        existingVenue.setCapacity(venueDetails.getCapacity());
        existingVenue.setAddress(venueDetails.getAddress());
        existingVenue.setCity(venueDetails.getCity());

        return venueRepository.save(existingVenue);
    }

    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede non trovata con id: " + id);
        }
        venueRepository.deleteById(id);
    }
}
