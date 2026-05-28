package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "feedback",
        // Vincolo UNIQUE tra user_id e event_id per evitare feedback duplicati
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_user_event_feedback",
                        columnNames = {"user_id", "event_id"}
                )
        }
)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String feedbackText;

    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    @PrePersist()
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate()
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
