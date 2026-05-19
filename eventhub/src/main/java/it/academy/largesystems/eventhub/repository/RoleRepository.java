package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleUser);
}
