package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.Ticket;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import it.academy.largesystems.eventhub.exception.ResourceConflictException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.EventRepository;
import it.academy.largesystems.eventhub.repository.TicketRepository;
import it.academy.largesystems.eventhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public Ticket createBooking(Long userId, Long eventId, TicketType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato"));

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        if (event.getEventDate().isBefore(today) ||
                (event.getEventDate().isEqual(today) && event.getStartTime().isBefore(currentTime))) {
            throw new ResourceConflictException("Impossibile prenotare: l'evento è già iniziato o si è concluso.");
        }

        if (ticketRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new ResourceConflictException("Hai già effettuato una prenotazione per questo evento. Non puoi riprenotarti.");
        }

        // Calcolo dei posti disponibili in base a quanti biglietti attivi ci sono
        int venueCapacity = event.getVenue().getCapacity();
        long bookedTickets = ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.PRENOTATO);
        if (bookedTickets >= venueCapacity) {
            throw new ResourceConflictException("Posti esauriti nella struttura (" + event.getVenue().getName() + ") per questo evento!");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setType(type);
        ticket.setStatus(TicketStatus.PRENOTATO);

        if (type == TicketType.VIP) {
            ticket.setPrice(event.getPriceVip());
        } else {
            ticket.setPrice(event.getPriceStandard());
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket deleteBooking(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Biglietto non trovato"));

        if (ticket.getStatus() == TicketStatus.ANNULLATO) {
            throw new ResourceConflictException("Questo biglietto è già stato annullato.");
        }

        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        Event event = ticket.getEvent();

        if (event.getEventDate().isBefore(today) ||
                (event.getEventDate().isEqual(today) && event.getStartTime().isBefore(time))) {
            throw new ResourceConflictException("Impossibile annullare la prenotazione: l'evento è già iniziato o concluso.");
        }

        ticket.setStatus(TicketStatus.ANNULLATO);

        return ticketRepository.save(ticket);
    }
}
