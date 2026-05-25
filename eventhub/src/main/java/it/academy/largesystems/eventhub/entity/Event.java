package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome dell'evento è obbligatorio.")
    @Size(max = 150, message = "Il nome dell'evento non può superare i 150 caratteri.")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "La data dell'evento è obbligatoria.")
    @FutureOrPresent(message = "La data dell'evento non può essere nel passato.")
    @Column(nullable = false)
    private LocalDate eventDate;

    @NotNull(message = "L'orario di inizio è obbligatorio.")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "L'orario di fine è obbligatorio.")
    @Column(nullable = false)
    private LocalTime endTime;

    @PositiveOrZero(message = "Il prezzo Standard non può essere negativo.")
    @Column(nullable = false)
    private double priceStandard;

    @PositiveOrZero(message = "Il prezzo VIP non può essere negativo.")
    @Column(nullable = false)
    private double priceVip;

    private Instant createdAt;
    private Instant updatedAt;

    @NotNull(message = "La struttura (venue) è obbligatoria.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // logica per organizer nel service
    private User organizer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_tag",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags; // Uso set e non list per evitare duplicati

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_speaker",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id")
    )
    private List<Speaker> speakers;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}