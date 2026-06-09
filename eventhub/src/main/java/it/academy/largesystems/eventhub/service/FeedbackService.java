package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.FeedbackRequestDTO;
import it.academy.largesystems.eventhub.dto.FeedbackResponseDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackResponseDTO> dtoList = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackResponseDTO dto = new FeedbackResponseDTO();
            dto.setId(feedback.getId());
            dto.setRating(feedback.getRating());
            dto.setFeedbackText(feedback.getFeedbackText());

            if (feedback.getUser() != null) {
                dto.setUserId(feedback.getUser().getId());
                dto.setUsername(feedback.getUser().getUsername());
            }

            if (feedback.getEvent() != null) {
                dto.setEventId(feedback.getEvent().getId());
                dto.setEventName(feedback.getEvent().getName());
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedbacksByEventId(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("L'ID dell'evento non può essere nullo");
        }

        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);
        List<FeedbackResponseDTO> dtoList = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackResponseDTO dto = new FeedbackResponseDTO();
            dto.setId(feedback.getId());
            dto.setRating(feedback.getRating());
            dto.setFeedbackText(feedback.getFeedbackText());

            if (feedback.getUser() != null) {
                dto.setUserId(feedback.getUser().getId());
                dto.setUsername(feedback.getUser().getUsername());
            }

            if (feedback.getEvent() != null) {
                dto.setEventId(feedback.getEvent().getId());
                dto.setEventName(feedback.getEvent().getName());
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public void createFeedback(FeedbackRequestDTO request) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + request.getEventId()));

        LocalDateTime endDataAndTimeOfEvent = LocalDateTime.of(event.getEventDate(), event.getEndTime());
        if (LocalDateTime.now().isBefore(endDataAndTimeOfEvent)) {
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
    public FeedbackResponseDTO updateFeedback(Long id, FeedbackRequestDTO request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback non trovato con ID: " + id));

        feedback.setRating(request.getRating());
        feedback.setFeedbackText(request.getFeedbackText());

        Feedback updatedFeedback = feedbackRepository.save(feedback);

        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setId(updatedFeedback.getId());
        dto.setRating(updatedFeedback.getRating());
        dto.setFeedbackText(updatedFeedback.getFeedbackText());

        if (updatedFeedback.getUser() != null) {
            dto.setUserId(updatedFeedback.getUser().getId());
            dto.setUsername(updatedFeedback.getUser().getUsername());
        }

        if (updatedFeedback.getEvent() != null) {
            dto.setEventId(updatedFeedback.getEvent().getId());
            dto.setEventName(updatedFeedback.getEvent().getName());
        }

        return dto;
    }

    @Transactional
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback non trovato con ID: " + id));

        feedbackRepository.delete(feedback);
    }
}