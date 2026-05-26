package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.entity.Role;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ResourceConflictException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.RoleRepository;
import it.academy.largesystems.eventhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(UserRepository userRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceConflictException("Email già registrata");
        }

        Role role = roleRepository.findByName("ROLE_USER");

        if (role == null) {
            throw new ResourceNotFoundException("Configurazione di sistema incompleta: Ruolo ROLE_USER non trovato.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setBanned(false);

        userRepository.save(user);
    }
}

