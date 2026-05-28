package it.academy.largesystems.eventhub.entity;

import it.academy.largesystems.eventhub.entity.enums.TicketStatus;
import it.academy.largesystems.eventhub.entity.enums.TicketType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "ticket",
        // Vincolo UNIQUE tra user_id e event_id per evitare prenotazioni duplicate
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_user_event_ticket",
                        columnNames = {"user_id", "event_id"}
                )
        }
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La tipologia di biglietto è obbligatoria.")
    @Enumerated(EnumType.STRING)
    private TicketType type;

    @PositiveOrZero(message = "Il prezzo del biglietto non può essere negativo.")
    private double price;

    @NotNull(message = "Lo stato del biglietto è obbligatorio.")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull(message = "L'utente associato al biglietto è obbligatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "L'evento associato al biglietto è obbligatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

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