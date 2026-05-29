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
        SELECT e FROM Event e 
        LEFT JOIN e.venue v 
        LEFT JOIN e.organizer o
        WHERE e.eventDate = :date
          AND (v.name ILIKE %:venueName%)
          AND (o.email ILIKE %:organizerName%)
          AND (:tagName = '' OR EXISTS (
                SELECT t FROM e.tags t WHERE t.name ILIKE %:tagName%
              ))
    """,
            countQuery = """
        SELECT COUNT(e) FROM Event e 
        LEFT JOIN e.venue v 
        LEFT JOIN e.organizer o
        WHERE e.eventDate = :date
          AND (v.name ILIKE %:venueName%)
          AND (o.email ILIKE %:organizerName%)
          AND (:tagName = '' OR EXISTS (
                SELECT t FROM e.tags t WHERE t.name ILIKE %:tagName%
              ))
    """)
    Page<Event> findByFiltersWithDate(
            @Param("date") LocalDate date,
            @Param("tagName") String tagName,
            @Param("venueName") String venueName,
            @Param("organizerName") String organizerName,
            Pageable pageable
    );

    @Query(value = """
        SELECT e FROM Event e 
        LEFT JOIN e.venue v 
        LEFT JOIN e.organizer o
        WHERE (v.name ILIKE %:venueName%)
          AND (o.email ILIKE %:organizerName%)
          AND (:tagName = '' OR EXISTS (
                SELECT t FROM e.tags t WHERE t.name ILIKE %:tagName%
              ))
    """,
            countQuery = """
        SELECT COUNT(e) FROM Event e 
        LEFT JOIN e.venue v 
        LEFT JOIN e.organizer o
        WHERE (v.name ILIKE %:venueName%)
          AND (o.email ILIKE %:organizerName%)
          AND (:tagName = '' OR EXISTS (
                SELECT t FROM e.tags t WHERE t.name ILIKE %:tagName%
              ))
    """)
    Page<Event> findByFiltersWithoutDate(
            @Param("tagName") String tagName,
            @Param("venueName") String venueName,
            @Param("organizerName") String organizerName,
            Pageable pageable
    );
}
