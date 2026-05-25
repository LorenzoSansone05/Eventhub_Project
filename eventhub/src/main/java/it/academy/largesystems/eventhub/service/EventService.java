package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.Speaker;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.EventRepository;
import it.academy.largesystems.eventhub.repository.SpeakerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
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

    @Transactional(readOnly = true)
    public Page<Event> getEventsByFilters(LocalDate date, String tag, String venueName, String organizerName, Pageable pageable) {
        return eventRepository.findByFilters(date, tag, venueName, organizerName, pageable);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
    }

    @Transactional
    public Event createEvent(Event event) {
        User currentUser = getAuthenticatedUser();
        if (!hasRole(currentUser, "ORGANIZER")) {
            throw new ValidationException("L'utente loggato non ha il ruolo di ORGANIZER");
        }

        event.setOrganizer(currentUser);

        // Validazione e associazione speakers in fase di creazione
        if (event.getSpeakers() != null && !event.getSpeakers().isEmpty()) {
            event.setSpeakers(validationSpeakers(event.getSpeakers()));
        }

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        User currentUser = getAuthenticatedUser();
        Event event = getEventById(id);

        boolean isAdmin = hasRole(currentUser, "ADMIN");
        boolean isOwner = event.getOrganizer() != null
                && event.getOrganizer().getId().equals(currentUser.getId())
                && hasRole(currentUser, "ORGANIZER");

        if (!isOwner && !isAdmin) {
            throw new ValidationException("Non hai i permessi per modificare questo evento.");
        }

        event.setName(eventDetails.getName());
        event.setEventDate(eventDetails.getEventDate());
        event.setStartTime(eventDetails.getStartTime());
        event.setEndTime(eventDetails.getEndTime());
        event.setVenue(eventDetails.getVenue());

        if (eventDetails.getTags() != null) {
            event.setTags(eventDetails.getTags());
        }

        if (eventDetails.getSpeakers() != null) {
            event.setSpeakers(validationSpeakers(eventDetails.getSpeakers()));
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        User currentUser = getAuthenticatedUser();
        Event event = getEventById(id);

        boolean isAdmin = hasRole(currentUser, "ADMIN");

        boolean isOwner = event.getOrganizer() != null
                && event.getOrganizer().getId().equals(currentUser.getId())
                && hasRole(currentUser, "ORGANIZER");

        if (isOwner || isAdmin) {
            eventRepository.delete(event);
        } else {
            throw new ValidationException("Non hai i permessi per eliminare questo evento.");
        }
    }

    // CONTROLLO SPEAKER NEL DB
    private List<Speaker> validationSpeakers(List<Speaker> speakersFromWeb) {
        List<Speaker> speakers = new ArrayList<>();

        for (Speaker s : speakersFromWeb) {
            if (s.getId() == null) {
                throw new ValidationException("Impossibile creare un relatore da qui. Fornire solo ID esistenti.");
            }

            Speaker speakerDB = speakerRepository.findById(s.getId())
                    .orElseThrow(() -> new ValidationException("Il relatore con ID " + s.getId() + " non esiste nel sistema. Operazione annullata."));

            speakers.add(speakerDB);
        }

        return speakers;
    }
}