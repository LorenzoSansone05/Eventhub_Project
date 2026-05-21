package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

}