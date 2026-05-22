package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
