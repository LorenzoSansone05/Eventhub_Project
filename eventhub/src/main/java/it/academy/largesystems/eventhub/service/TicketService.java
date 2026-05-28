package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.BookTicketResponseDTO;
import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.Ticket;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceConflictException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.EventRepository;
import it.academy.largesystems.eventhub.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public BookTicketResponseDTO createBooking(Long eventId, TicketType type) {
        User user = securityUtil.getAuthenticatedUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato"));

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        if (event.getEventDate().isBefore(today) ||
                (event.getEventDate().isEqual(today) && event.getStartTime().isBefore(currentTime))) {
            throw new ResourceConflictException("Impossibile prenotare: l'evento è già iniziato o si è concluso.");
        }

        if (ticketRepository.existsByUserIdAndEventId(user.getId(), eventId)) {
            throw new ResourceConflictException("Hai già effettuato una prenotazione per questo evento. Non puoi riprenotarti.");
        }

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
        ticket.setPrice(type == TicketType.VIP ? event.getPriceVip() : event.getPriceStandard());

        Ticket createdTicket = ticketRepository.save(ticket);

        return new BookTicketResponseDTO(
                createdTicket.getId(), event.getId(), event.getName(), event.getEventDate(),
                user.getId(), user.getEmail(), createdTicket.getType(), createdTicket.getStatus(), createdTicket.getPrice()
        );
    }

    @Transactional
    public void deleteBooking(Long ticketId) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Biglietto non trovato"));

        boolean isOwner = ticket.getUser() != null && ticket.getUser().getId().equals(currentUser.getId());

        if (!isOwner) {
            throw new ForbiddenException("Non hai i permessi per annullare questo biglietto, non ti appartiene.");
        }

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
        ticketRepository.save(ticket);
    }
}