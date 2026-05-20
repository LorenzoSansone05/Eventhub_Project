package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.dto.ProfileUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.Profile;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profilo non trovato per l'utente con ID: " + userId));
    }

    @Transactional
    public Profile updateProfileByUserId(Long userId, ProfileUpdateRequestDTO request) {
        Profile profile = getProfileByUserId(userId);

        profile.setName(request.getName());
        profile.setSurname(request.getSurname());
        profile.setBirth_date(request.getBirthDate());
        profile.setCity(request.getCity());
        profile.setDescription(request.getDescription());

        return profileRepository.save(profile);
    }

}