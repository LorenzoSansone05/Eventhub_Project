package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.Ticket;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
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
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento non trovato"));

        LocalDate oggi = LocalDate.now();
        LocalTime oraCorrente = LocalTime.now();

        if (event.getEventDate().isBefore(oggi) ||
                (event.getEventDate().isEqual(oggi) && event.getStartTime().isBefore(oraCorrente))) {
            throw new IllegalStateException("Impossibile prenotare: l'evento è già iniziato o concluso.");
        }

        if (ticketRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("Hai già effettuato una prenotazione per questo evento. Non puoi riprenotarti.");
        }

        int venueCapacity = event.getVenue().getCapacity();
        long activeTickets = ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.PRENOTATO);

        if (activeTickets >= venueCapacity) {
            throw new IllegalStateException("Posti esauriti nella struttura (" + event.getVenue().getName() + ") per questo evento");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setType(type);
        ticket.setStatus(TicketStatus.PRENOTATO);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket deleteBooking(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Biglietto non trovato"));

        if (ticket.getStatus() == TicketStatus.ANNULLATO) {
            throw new IllegalStateException("Questo biglietto è già stato annullato.");
        }

        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        Event event = ticket.getEvent();

        if (event.getEventDate().isBefore(today) ||
                (event.getEventDate().isEqual(today) && event.getStartTime().isBefore(time))) {
            throw new IllegalStateException("Impossibile annullare la prenotazione: l'evento è già iniziato o concluso.");
        }

        ticket.setStatus(TicketStatus.ANNULLATO);

        return ticketRepository.save(ticket);
    }
}
