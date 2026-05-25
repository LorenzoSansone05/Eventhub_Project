package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Il nome del relatore è obbligatorio.")
    @Size(max = 60, message = "Il nome non può superare i 60 caratteri.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Il cognome del relatore è obbligatorio.")
    @Size(max = 60, message = "Il cognome non può superare i 60 caratteri.")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "La biografia del relatore è obbligatoria.")
    @Size(max = 1000, message = "La biografia non può superare i 1000 caratteri.")
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