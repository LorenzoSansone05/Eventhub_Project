package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    // Questo metodo recupera dal contesto di sicurezza spring i dati dell'utente di quel thread, andando a rifiutare chi non ha effettuato la login
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("Operazione negata: Devi effettuare il login.");
        }

        return (User) authentication.getPrincipal();
    }

    // 1. Si assicura che l'utente abbia un ruolo 2. Confronta l'uguaglianza tra la stringa passata in input nel metodo con il ruolo dell'utente passato in input
    private boolean hasRole(User user, String roleName) {
        if (user == null || user.isBanned()) {
            return false;
        }
        return user.getRole() != null && roleName.equalsIgnoreCase(user.getRole().getName());
    }

    // TUTTI

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato con ID: " + id));
    }

    // ADMIN && ORGANIZER

    public Event createEvent(Event event) {
        User currentUser = getAuthenticatedUser();
        boolean isOrganizer = hasRole(currentUser, "ORGANIZER");

        if (isOrganizer) {
            event.setOrganizer(currentUser);
            return eventRepository.save(event);
        } else {
            throw new ValidationException("L'utente loggato non ha il ruolo di ORGANIZER");
        }
    }

    public Event updateEvent(Long id, Event eventDetails) {
        User currentUser = getAuthenticatedUser();
        Event event = getEventById(id);

        boolean isOwner = event.getOrganizer().getId().equals(currentUser.getId()) && hasRole(currentUser, "ORGANIZER");
        boolean isAdmin = hasRole(currentUser, "ADMIN");

        if (isOwner || isAdmin) {
            event.setName(eventDetails.getName());
            event.setEventDate(eventDetails.getEventDate());
            event.setStartTime(eventDetails.getStartTime());
            event.setEndTime(eventDetails.getEndTime());
            event.setVenue(eventDetails.getVenue());
            event.setTags(eventDetails.getTags());

            return eventRepository.save(event);
        } else {
            throw new SecurityException("Non hai i permessi per modificare questo evento.");
        }
    }

    public void deleteEvent(Long id) {
        User currentUser = getAuthenticatedUser();
        Event event = getEventById(id);


        boolean isOwner = event.getOrganizer().getId().equals(currentUser.getId()) && hasRole(currentUser, "ORGANIZER");
        boolean isAdmin = hasRole(currentUser, "ADMIN");

        if (isOwner || isAdmin) {
            eventRepository.delete(event);
        } else {
            throw new SecurityException("Non hai i permessi per eliminare questo evento.");
        }
    }
}