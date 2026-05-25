package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Speaker;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.SpeakerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("Operazione negata: Devi effettuare il login.");
        }
        return (User) authentication.getPrincipal();
    }

    private boolean hasRole(User user, String roleName) {
        if (user == null || user.isBanned()) {
            return false;
        }
        return user.getRole() != null && roleName.equalsIgnoreCase(user.getRole().getName());
    }

    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    public Speaker getSpeakerById(Long id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker non trovato con ID: " + id));
    }

    // SOLO ADMIN può creare
    @Transactional
    public Speaker createSpeaker(Speaker speaker) {
        User currentUser = getAuthenticatedUser();
        if (hasRole(currentUser, "ADMIN")) {
            return speakerRepository.save(speaker);
        }
        throw new ForbiddenException("Solo l'amministratore può censire un nuovo relatore.");
    }

    // SOLO ADMIN può modificare
    @Transactional
    public Speaker updateSpeaker(Long id, Speaker speakerDetails) {
        User currentUser = getAuthenticatedUser();
        if (hasRole(currentUser, "ADMIN")) {
            Speaker speaker = getSpeakerById(id);
            speaker.setName(speakerDetails.getName());
            speaker.setSurname(speakerDetails.getSurname());
            speaker.setBio(speakerDetails.getBio());
            return speakerRepository.save(speaker);
        }
        throw new ForbiddenException("Solo l'amministratore può modificare i dati di un relatore.");
    }

    // SOLO ADMIN può eliminare
    @Transactional
    public void deleteSpeaker(Long id) {
        User currentUser = getAuthenticatedUser();
        if (hasRole(currentUser, "ADMIN")) {
            Speaker speaker = getSpeakerById(id);
            speakerRepository.delete(speaker);
        } else {
            throw new ForbiddenException("Solo l'amministratore può eliminare un relatore.");
        }
    }
}