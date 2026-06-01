package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.EventCreateRequestDTO;
import it.academy.largesystems.eventhub.dto.EventResponseDTO;
import it.academy.largesystems.eventhub.dto.EventSummaryResponseDTO;
import it.academy.largesystems.eventhub.entity.*;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final VenueRepository venueRepository;
    private final TagRepository tagRepository;
    private final SecurityUtil securityUtil;

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByFilters(LocalDate date, String tag, String venueName, String organizerName, Pageable pageable) {

        String cleanTag = (tag == null) ? "" : tag;
        String cleanVenue = (venueName == null) ? "" : venueName;
        String cleanOrganizer = (organizerName == null) ? "" : organizerName;

        Page<Event> eventPage;

        if (date == null) {
            eventPage = eventRepository.findByFiltersWithoutDate(cleanTag, cleanVenue, cleanOrganizer, pageable);
        } else {
            eventPage = eventRepository.findByFiltersWithDate(date, cleanTag, cleanVenue, cleanOrganizer, pageable);
        }

        List<EventResponseDTO> dtoList = new ArrayList<>();

        for (Event event : eventPage.getContent()) {
            EventResponseDTO dto = new EventResponseDTO();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setEventDate(event.getEventDate());
            dto.setStartTime(event.getStartTime());
            dto.setEndTime(event.getEndTime());
            dto.setPriceStandard(event.getPriceStandard());
            dto.setPriceVip(event.getPriceVip());

            if (event.getVenue() != null) {
                dto.setVenueId(event.getVenue().getId());
                dto.setVenueName(event.getVenue().getName());
                dto.setVenueCity(event.getVenue().getCity());
                dto.setVenueAddress(event.getVenue().getAddress());
            }

            if (event.getOrganizer() != null) {
                dto.setOrganizerId(event.getOrganizer().getId());
                dto.setOrganizerEmail(event.getOrganizer().getEmail());
            }

            if (event.getTags() != null) {
                Set<String> tagNames = new HashSet<>();
                for (Tag t : event.getTags()) {
                    tagNames.add(t.getName());
                }
                dto.setTags(tagNames);
            }

            if (event.getSpeakers() != null) {
                List<String> speakerNames = new ArrayList<>();
                for (Speaker s : event.getSpeakers()) {
                    speakerNames.add(s.getName() + " " + s.getSurname());
                }
                dto.setSpeakerNames(speakerNames);
            }

            if (event.getFeedbacks() != null) {
                List<String> feedbackStrings = new ArrayList<>();
                for (Feedback f : event.getFeedbacks()) {
                    if (f.getUser() == null) {
                        throw new ResourceNotFoundException("Integrità dati violata: Trovato un feedback (ID: " + f.getId() + ") senza un utente associato.");
                    }
                    String userEmail = f.getUser().getEmail();
                    String commento = (f.getFeedbackText() != null && !f.getFeedbackText().isBlank()) ? " - " + f.getFeedbackText() : "";
                    feedbackStrings.add(f.getRating() + "/5" + commento + " (" + userEmail + ")");
                }
                dto.setFeedbacks(feedbackStrings);
            }

            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, eventPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + id));

        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setEventDate(event.getEventDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setPriceStandard(event.getPriceStandard());
        dto.setPriceVip(event.getPriceVip());

        if (event.getVenue() != null) {
            dto.setVenueId(event.getVenue().getId());
            dto.setVenueName(event.getVenue().getName());
            dto.setVenueCity(event.getVenue().getCity());
            dto.setVenueAddress(event.getVenue().getAddress());
        }

        if (event.getOrganizer() != null) {
            dto.setOrganizerId(event.getOrganizer().getId());
            dto.setOrganizerEmail(event.getOrganizer().getEmail());
        }

        if (event.getTags() != null) {
            Set<String> tagNames = new HashSet<>();
            for (Tag t : event.getTags()) {
                tagNames.add(t.getName());
            }
            dto.setTags(tagNames);
        }

        if (event.getSpeakers() != null) {
            List<String> speakerNames = new ArrayList<>();
            for (Speaker s : event.getSpeakers()) {
                speakerNames.add(s.getName() + " " + s.getSurname());
            }
            dto.setSpeakerNames(speakerNames);
        }

        if (event.getFeedbacks() != null) {
            List<String> feedbackStrings = new ArrayList<>();
            for (Feedback f : event.getFeedbacks()) {
                if (f.getUser() == null) {
                    throw new ResourceNotFoundException("Integrità dati violata: Trovato un feedback (ID: " + f.getId() + ") senza un utente associato.");
                }
                String userEmail = f.getUser().getEmail();
                String commento = (f.getFeedbackText() != null && !f.getFeedbackText().isBlank()) ? " - " + f.getFeedbackText() : "";
                feedbackStrings.add(f.getRating() + "/5" + commento + " (" + userEmail + ")");
            }
            dto.setFeedbacks(feedbackStrings);
        }

        return dto;
    }

    @Transactional
    public EventResponseDTO createEvent(EventCreateRequestDTO dto) {
        User currentUser = securityUtil.getAuthenticatedUser();

        if (dto.getPriceStandard() > dto.getPriceVip()) {
            throw new ValidationException("Errore di configurazione prezzi: il prezzo Standard non può superare il prezzo VIP.");
        }

        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Struttura non trouvata con ID: " + dto.getVenueId()));

        Event event = new Event();
        event.setName(dto.getName());
        event.setEventDate(dto.getEventDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setPriceStandard(dto.getPriceStandard());
        event.setPriceVip(dto.getPriceVip());
        event.setVenue(venue);
        event.setOrganizer(currentUser);

        if (dto.getSpeakerIds() != null && !dto.getSpeakerIds().isEmpty()) {
            event.setSpeakers(validationSpeakersById(dto.getSpeakerIds()));
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            event.setTags(new HashSet<>(tags));
        }

        Event savedEvent = eventRepository.save(event);


        EventResponseDTO responseDto = new EventResponseDTO();
        responseDto.setId(savedEvent.getId());
        responseDto.setName(savedEvent.getName());
        responseDto.setEventDate(savedEvent.getEventDate());
        responseDto.setStartTime(savedEvent.getStartTime());
        responseDto.setEndTime(savedEvent.getEndTime());
        responseDto.setPriceStandard(savedEvent.getPriceStandard());
        responseDto.setPriceVip(savedEvent.getPriceVip());

        if (savedEvent.getVenue() != null) {
            responseDto.setVenueId(savedEvent.getVenue().getId());
            responseDto.setVenueName(savedEvent.getVenue().getName());
            responseDto.setVenueCity(savedEvent.getVenue().getCity());
            responseDto.setVenueAddress(savedEvent.getVenue().getAddress());
        }

        if (savedEvent.getOrganizer() != null) {
            responseDto.setOrganizerId(savedEvent.getOrganizer().getId());
            responseDto.setOrganizerEmail(savedEvent.getOrganizer().getEmail());
        }

        if (savedEvent.getTags() != null) {
            Set<String> tagNames = new HashSet<>();
            for (Tag t : savedEvent.getTags()) {
                tagNames.add(t.getName());
            }
            responseDto.setTags(tagNames);
        }

        if (savedEvent.getSpeakers() != null) {
            List<String> speakerNames = new ArrayList<>();
            for (Speaker s : savedEvent.getSpeakers()) {
                speakerNames.add(s.getName() + " " + s.getSurname());
            }
            responseDto.setSpeakerNames(speakerNames);
        }

        responseDto.setFeedbacks(new ArrayList<>());

        return responseDto;
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventCreateRequestDTO dto) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + id));

        boolean isOwner = event.getOrganizer() != null
                && event.getOrganizer().getId().equals(currentUser.getId());

        if (!isOwner) {
            throw new ForbiddenException("Non hai i permessi per modificare questo evento.");
        }

        if (dto.getPriceStandard() > dto.getPriceVip()) {
            throw new ValidationException("Errore di configurazione prezzi: il prezzo Standard non può superare il prezzo VIP.");
        }

        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Struttura non trovata con ID: " + dto.getVenueId()));

        event.setName(dto.getName());
        event.setEventDate(dto.getEventDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVenue(venue);
        event.setPriceStandard(dto.getPriceStandard());
        event.setPriceVip(dto.getPriceVip());

        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            event.setTags(new HashSet<>(tags));
        }

        if (dto.getSpeakerIds() != null) {
            event.setSpeakers(validationSpeakersById(dto.getSpeakerIds()));
        }

        Event savedEvent = eventRepository.save(event);

        EventResponseDTO responseDto = new EventResponseDTO();

        responseDto.setId(savedEvent.getId());
        responseDto.setName(savedEvent.getName());
        responseDto.setEventDate(savedEvent.getEventDate());
        responseDto.setStartTime(savedEvent.getStartTime());
        responseDto.setEndTime(savedEvent.getEndTime());
        responseDto.setPriceStandard(savedEvent.getPriceStandard());
        responseDto.setPriceVip(savedEvent.getPriceVip());

        if (savedEvent.getVenue() != null) {
            responseDto.setVenueId(savedEvent.getVenue().getId());
            responseDto.setVenueName(savedEvent.getVenue().getName());
            responseDto.setVenueCity(savedEvent.getVenue().getCity());
            responseDto.setVenueAddress(savedEvent.getVenue().getAddress());
        }

        if (savedEvent.getOrganizer() != null) {
            responseDto.setOrganizerId(savedEvent.getOrganizer().getId());
            responseDto.setOrganizerEmail(savedEvent.getOrganizer().getEmail());
        }

        if (savedEvent.getTags() != null) {
            Set<String> tagNames = new HashSet<>();
            for (Tag t : savedEvent.getTags()) {
                tagNames.add(t.getName());
            }
            responseDto.setTags(tagNames);
        }

        if (savedEvent.getSpeakers() != null) {
            List<String> speakerNames = new ArrayList<>();
            for (Speaker s : savedEvent.getSpeakers()) {
                speakerNames.add(s.getName() + " " + s.getSurname());
            }
            responseDto.setSpeakerNames(speakerNames);
        }

        if (savedEvent.getFeedbacks() != null) {
            List<String> feedbackStrings = new ArrayList<>();
            for (Feedback f : savedEvent.getFeedbacks()) {
                if (f.getUser() == null) {
                    throw new ResourceNotFoundException("Integrità dati violata: Trovato un feedback (ID: " + f.getId() + ") senza un utente associato.");
                }
                String userEmail = f.getUser().getEmail();
                String commento = (f.getFeedbackText() != null && !f.getFeedbackText().isBlank()) ? " - " + f.getFeedbackText() : "";
                feedbackStrings.add(f.getRating() + "/5" + commento + " (" + userEmail + ")");
            }
            responseDto.setFeedbacks(feedbackStrings);
        }

        return responseDto;
    }

    @Transactional
    public void deleteEvent(Long id) {
        User currentUser = securityUtil.getAuthenticatedUser();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + id));

        boolean isOwner = event.getOrganizer() != null
                && event.getOrganizer().getId().equals(currentUser.getId());

        if (!isOwner) {
            throw new ForbiddenException("Non hai i permessi per eliminare questo evento.");
        }

        if (event.getSpeakers() != null) {
            event.getSpeakers().clear();
        }

        if (event.getTags() != null) {
            event.getTags().clear();
        }

        eventRepository.saveAndFlush(event);
        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public String getEventRatingSummary(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento non trovato con ID: " + id));

        if (event.getFeedbacks() == null || event.getFeedbacks().isEmpty()) {
            return "0.0/5 (0 recensioni)";
        }

        double sumReviews = 0;
        int totalReviews = event.getFeedbacks().size();

        for (Feedback f : event.getFeedbacks()) {
            if (f.getUser() == null) {
                throw new ResourceNotFoundException("Integrità dati violata: Trovato un feedback senza utente associato.");
            }
            sumReviews += f.getRating();
        }

        double media = sumReviews / totalReviews;
        String mediaFormatted = String.format("%.1f", media);

        return mediaFormatted + "/5 basato su " + totalReviews + " recensioni.";
    }

    // EFFETTUA TANTE QUERY QUANTI SONO GLI ID INVIATI NELLA RICHIESTA
    private List<Speaker> validationSpeakersById(List<Long> speakerIds) {
        List<Speaker> speakers = new ArrayList<>();
        for (Long id : speakerIds) {
            Speaker speakerDB = speakerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Il relatore con ID " + id + " non esiste nel sistema."));
            speakers.add(speakerDB);
        }
        return speakers;
    }
}