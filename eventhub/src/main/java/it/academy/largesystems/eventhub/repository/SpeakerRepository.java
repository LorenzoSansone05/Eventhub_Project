package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

}
