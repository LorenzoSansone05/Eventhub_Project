package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String formattedName);
}
