package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome della struttura è obbligatorio.")
    @Size(max = 100, message = "Il nome della struttura non può superare i 100 caratteri.")
    private String name;

    @NotNull(message = "La capienza della struttura è obbligatoria.")
    @Min(value = 1, message = "La capienza deve essere di almeno 1 posto.")
    private int capacity;

    @NotBlank(message = "L'indirizzo della struttura è obbligatorio.")
    @Size(max = 150, message = "L'indirizzo non può superare i 150 caratteri.")
    private String address;

    @NotBlank(message = "La città è obbligatoria.")
    @Size(max = 100, message = "La città non può superare i 100 caratteri.")
    private String city;

    private Instant createdAt;

    @OneToMany(mappedBy = "venue")
    private List<Event> events;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}