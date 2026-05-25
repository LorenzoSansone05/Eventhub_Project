package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = """
        SELECT DISTINCT e FROM Event e 
        LEFT JOIN FETCH e.speakers 
        LEFT JOIN FETCH e.tags t 
        LEFT JOIN e.venue v
        LEFT JOIN e.organizer o
        WHERE (:date IS NULL OR e.eventDate = :date)
          AND (:tagName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%')))
          AND (:venueName IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :venueName, '%')))
          AND (:organizerName IS NULL OR LOWER(o.username) LIKE LOWER(CONCAT('%', :organizerName, '%')))
    """,
            countQuery = """
        SELECT COUNT(DISTINCT e) FROM Event e 
        LEFT JOIN e.tags t
        LEFT JOIN e.venue v
        LEFT JOIN e.organizer o
        WHERE (:date IS NULL OR e.eventDate = :date)
          AND (:tagName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%')))
          AND (:venueName IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :venueName, '%')))
          AND (:organizerName IS NULL OR LOWER(o.username) LIKE LOWER(CONCAT('%', :organizerName, '%')))
    """)
    Page<Event> findByFilters(
            @Param("date") LocalDate date,
            @Param("tagName") String tagName,
            @Param("venueName") String venueName,
            @Param("organizerName") String organizerName,
            Pageable pageable
    );
}
