package it.academy.largesystems.eventhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isBanned;

    @ManyToOne
    @JoinColumn(name = "role_id") // FK
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}
