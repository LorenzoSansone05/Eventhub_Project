package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Ticket;
import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    long countByEventIdAndStatus(Long eventId, TicketStatus status);

    List<Ticket> findByUserId(Long id);
}