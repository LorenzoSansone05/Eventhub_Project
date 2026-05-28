package it.academy.largesystems.eventhub.repository;

import it.academy.largesystems.eventhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u JOIN u.role r WHERE r.name = :role")
    long countByRoleName(@Param("role") String role);
}

