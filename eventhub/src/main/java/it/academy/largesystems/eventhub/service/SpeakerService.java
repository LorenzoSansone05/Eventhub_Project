package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.SpeakerRequestDTO;
import it.academy.largesystems.eventhub.dto.SpeakerResponseDTO;
import it.academy.largesystems.eventhub.entity.Speaker;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.SpeakerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final SecurityUtil securityUtil;

    private boolean hasRole(User user, String roleName) {
        if (user == null || user.isBanned()) {
            return false;
        }
        return user.getRole() != null && roleName.equalsIgnoreCase(user.getRole().getName());
    }

    @Transactional(readOnly = true)
    public List<SpeakerResponseDTO> getAllSpeakers() {
        List<Speaker> speakers = speakerRepository.findAll();
        List<SpeakerResponseDTO> dtoList = new ArrayList<>();

        for (Speaker s : speakers) {
            dtoList.add(new SpeakerResponseDTO(s.getId(), s.getName(), s.getSurname(), s.getBio()));
        }
        return dtoList;
    }

    @Transactional(readOnly = true)
    public SpeakerResponseDTO getSpeakerById(Long id) {
        Speaker s = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker non trovato con ID: " + id));

        return new SpeakerResponseDTO(s.getId(), s.getName(), s.getSurname(), s.getBio());
    }

    @Transactional
    public SpeakerResponseDTO createSpeaker(SpeakerRequestDTO dto) {
        User currentUser = securityUtil.getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può censire un nuovo relatore.");
        }

        Speaker speaker = new Speaker();
        speaker.setName(dto.getName());
        speaker.setSurname(dto.getSurname());
        speaker.setBio(dto.getBio());

        Speaker saved = speakerRepository.save(speaker);
        return new SpeakerResponseDTO(saved.getId(), saved.getName(), saved.getSurname(), saved.getBio());
    }

    @Transactional
    public SpeakerResponseDTO updateSpeaker(Long id, SpeakerRequestDTO dto) {
        User currentUser = securityUtil.getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può modificare i dati di un relatore.");
        }

        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker non trovato con ID: " + id));

        speaker.setName(dto.getName());
        speaker.setSurname(dto.getSurname());
        speaker.setBio(dto.getBio());

        Speaker updated = speakerRepository.save(speaker);
        return new SpeakerResponseDTO(updated.getId(), updated.getName(), updated.getSurname(), updated.getBio());
    }

    @Transactional
    public void deleteSpeaker(Long id) {
        User currentUser = securityUtil.getAuthenticatedUser();
        if (!hasRole(currentUser, "ADMIN")) {
            throw new ForbiddenException("Solo l'amministratore può eliminare un relatore.");
        }

        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker non trovato con ID: " + id));

        speakerRepository.delete(speaker);
    }
}