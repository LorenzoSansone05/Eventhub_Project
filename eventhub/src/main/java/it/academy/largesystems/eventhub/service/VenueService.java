package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.VenueRequestDTO;
import it.academy.largesystems.eventhub.dto.VenueResponseDTO;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.entity.Venue;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
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
    private final SecurityUtil securityUtil;

    private User getAuthenticatedUser() {
        return securityUtil.getAuthenticatedUser();
    }

    private boolean hasRole(User user, String roleName) {
        if (user == null || user.isBanned()) {
            return false;
        }
        return user.getRole() != null && roleName.equalsIgnoreCase(user.getRole().getName());
    }

    @Transactional
    public VenueResponseDTO createVenue(VenueRequestDTO venueRequest) {
        User currentUser = getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può registrare una nuova struttura.");
        }

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
        User currentUser = getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può modificare i dati di una struttura.");
        }

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
        User currentUser = getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può eliminare una struttura.");
        }

        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sede non trovata con id: " + id);
        }
        venueRepository.deleteById(id);
    }
}