package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 60, message = "Il nome non può superare i 60 caratteri.")
    private String name;

    @Size(max = 60, message = "Il cognome non può superare i 60 caratteri.")
    private String surname;

    @Past(message = "La data di nascita deve essere nel passato.")
    private LocalDate birth_date;

    @Size(max = 100, message = "La città non può superare i 100 caratteri.")
    private String city;

    private Instant createdAt;

    private Instant updatedAt;

    @Size(max = 500, message = "La descrizione non può superare i 500 caratteri.")
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}