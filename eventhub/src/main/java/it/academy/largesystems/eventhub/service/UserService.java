package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Role;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.RoleRepository;
import it.academy.largesystems.eventhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // Uso @Transactional anche se non effettuo operazioni che modificano il DB, per non far chiudere la sessione in caso dovessi fare un caricamento dei dati in modalita LAZY

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con email: " + email));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Transactional
    public void updateEmail(Long id, String newEmail) {
        User user = getUserById(id);

        // Query per verificare se esiste gia un altro utente con la stessa email collegata
        userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new ValidationException("Email già in uso da un altro account.");
            }
        });

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = getUserById(id);

        // Confronto della vecchia password immessa da lui e quella che nopi abbiamo salvato sul db
        // Fa il confronto anche se la password è stata cryptata, grazie al metodo matches di springSecurity
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ValidationException("La vecchia password non è corretta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRole(Long id, String newRoleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + id));

        String formattedRoleName = newRoleName.toUpperCase();

        if (!formattedRoleName.equals("ORGANIZER") && !formattedRoleName.equals("USER")) {
            throw new ValidationException("Ruolo non valido. È possibile assegnare solo 'ORGANIZER' o 'USER'.");
        }

        Role targetRole = roleRepository.findByName(formattedRoleName);

        user.setRole(targetRole);
        userRepository.save(user);
    }
}
