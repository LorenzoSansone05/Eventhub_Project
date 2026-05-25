package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String bio;

    private Instant createdAt;

    @ManyToMany(mappedBy = "speakers", fetch = FetchType.LAZY)
    private List<Event> events;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}
