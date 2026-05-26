package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.UserResponseDTO;
import it.academy.largesystems.eventhub.entity.Role;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ResourceConflictException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.exception.ValidationException;
import it.academy.largesystems.eventhub.repository.RoleRepository;
import it.academy.largesystems.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SecurityUtil securityUtil;

    private User getAuthenticatedUser() {
        return securityUtil.getAuthenticatedUser();
    }

    // Uso @Transactional anche se non effettuo operazioni che modificano il DB, per non far chiudere la sessione in caso dovessi fare un caricamento dei dati in modalita LAZY

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponseDTO> responseList = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setBanned(user.isBanned());

            if (user.getRole() != null) {
                dto.setRoleName(user.getRole().getName());
            }

            responseList.add(dto);
        }

        return responseList;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setBanned(user.isBanned());

        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }

        return dto;
    }

//    @Transactional(readOnly = true)
//    public UserResponseDTO getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con email: " + email));
//
//        UserResponseDTO dto = new UserResponseDTO();
//        dto.setId(user.getId());
//        dto.setEmail(user.getEmail());
//        dto.setBanned(user.isBanned());
//
//        if (user.getRole() != null) {
//            dto.setRoleName(user.getRole().getName());
//        }
//
//        return dto;
//    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public void updateEmail(String newEmail) {
        User user = getAuthenticatedUser();

        userRepository.findByEmail(newEmail).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(user.getId())) {
                throw new ResourceConflictException("Email già in uso da un altro account.");
            }
        });

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String oldPassword, String newPassword) {
        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ValidationException("La vecchia password non è corretta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRole(Long id, String newRoleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + id));

        String formattedRoleName = newRoleName.toUpperCase();

        if (!formattedRoleName.equals("ORGANIZER") && !formattedRoleName.equals("USER")) {
            throw new ResourceConflictException("Ruolo non valido. È possibile assegnare solo 'ORGANIZER' o 'USER'.");
        }

        Role targetRole = roleRepository.findByName(formattedRoleName);

        user.setRole(targetRole);
        userRepository.save(user);
    }
}
