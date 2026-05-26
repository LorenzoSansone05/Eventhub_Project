package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.FeedbackRequestDTO;
import it.academy.largesystems.eventhub.entity.Event;
import it.academy.largesystems.eventhub.entity.Feedback;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.EventRepository;
import it.academy.largesystems.eventhub.repository.FeedbackRepository;
import it.academy.largesystems.eventhub.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public void createFeedback(FeedbackRequestDTO request) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + request.getEventId()));

        LocalDateTime dataOraFineEvento = LocalDateTime.of(event.getEventDate(), event.getEndTime());
        if (LocalDateTime.now().isBefore(dataOraFineEvento)) {
            throw new ValidationException("Non puoi lasciare un feedback prima che l'evento sia terminato.");
        }

        boolean hasTicket = ticketRepository.existsByUserIdAndEventId(currentUser.getId(), event.getId());
        if (!hasTicket) {
            throw new ForbiddenException("Non puoi recensire un evento a cui non hai partecipato.");
        }

        Feedback feedback = new Feedback();
        feedback.setRating(request.getRating());
        feedback.setFeedbackText(request.getFeedbackText());
        feedback.setUser(currentUser);
        feedback.setEvent(event);

        feedbackRepository.save(feedback);
    }

    @Transactional
    public void updateFeedback(Long id, FeedbackRequestDTO request) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback non trovato con ID: " + id));

        if (!feedback.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Non hai i permessi per modificare questo feedback.");
        }

        feedback.setRating(request.getRating());
        feedback.setFeedbackText(request.getFeedbackText());

        feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback non trovato con ID: " + id));

        boolean isAdmin = currentUser.getRole() != null && "ADMIN".equalsIgnoreCase(currentUser.getRole().getName());

        if (!isAdmin) {
            throw new ForbiddenException("Non hai i permessi per eliminare questo feedback.");
        }

        feedbackRepository.delete(feedback);
    }
}