package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.ProfileResponseDTO;
import it.academy.largesystems.eventhub.dto.ProfileUpdateRequestDTO;
import it.academy.largesystems.eventhub.entity.Profile;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileByUserId() {
        User currentUser = securityUtil.getAuthenticatedUser();

        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profilo non trovato per l'utente corrente."));

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhotoUrl(profile.getPhotoUrl());
        dto.setBirthDate(profile.getBirth_date());
        dto.setCity(profile.getCity());
        dto.setDescription(profile.getDescription());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setEmail(profile.getUser().getEmail());
        }
        return dto;
    }

    @Transactional
    public ProfileResponseDTO updateProfileByUserId(ProfileUpdateRequestDTO request) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profilo non trovato per l'utente corrente."));

        profile.setName(request.getName());
        profile.setSurname(request.getSurname());
        profile.setBirth_date(request.getBirthDate());
        profile.setPhotoUrl(request.getPhotoUrl());
        profile.setCity(request.getCity());
        profile.setDescription(request.getDescription());

        Profile updatedProfile = profileRepository.save(profile);

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhotoUrl(profile.getPhotoUrl());
        dto.setBirthDate(profile.getBirth_date());
        dto.setCity(profile.getCity());
        dto.setDescription(profile.getDescription());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setEmail(profile.getUser().getEmail());
        }
        return dto;
    }
}