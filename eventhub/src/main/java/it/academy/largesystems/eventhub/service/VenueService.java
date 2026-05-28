package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.dto.VenueRequestDTO;
import it.academy.largesystems.eventhub.dto.VenueResponseDTO;
import it.academy.largesystems.eventhub.entity.Venue;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    @Transactional
    public VenueResponseDTO createVenue(VenueRequestDTO venueRequest) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getName());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setAddress(venueRequest.getAddress());
        venue.setCity(venueRequest.getCity());

        Venue saved = venueRepository.save(venue);
        return new VenueResponseDTO(saved.getId(), saved.getName(), saved.getCapacity(), saved.getAddress(), saved.getCity());
    }

    @Transactional(readOnly = true)
    public List<VenueResponseDTO> getAllVenues() {
        List<Venue> venues = venueRepository.findAll();
        List<VenueResponseDTO> dtoList = new ArrayList<>();
        for (Venue v : venues) {
            dtoList.add(new VenueResponseDTO(v.getId(), v.getName(), v.getCapacity(), v.getAddress(), v.getCity()));
        }
        return dtoList;
    }

    @Transactional(readOnly = true)
    public VenueResponseDTO getVenueById(Long id) {
        Venue v = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id: " + id));
        return new VenueResponseDTO(v.getId(), v.getName(), v.getCapacity(), v.getAddress(), v.getCity());
    }

    @Transactional
    public VenueResponseDTO updateVenue(Long id, VenueRequestDTO venueDetails) {
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id: " + id));

        existingVenue.setName(venueDetails.getName());
        existingVenue.setCapacity(venueDetails.getCapacity());
        existingVenue.setAddress(venueDetails.getAddress());
        existingVenue.setCity(venueDetails.getCity());

        Venue updated = venueRepository.save(existingVenue);
        return new VenueResponseDTO(updated.getId(), updated.getName(), updated.getCapacity(), updated.getAddress(), updated.getCity());
    }

    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede non trovata con id: " + id);
        }
        venueRepository.deleteById(id);
    }
}