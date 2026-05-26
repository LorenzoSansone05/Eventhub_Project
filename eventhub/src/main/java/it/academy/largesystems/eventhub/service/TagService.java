package it.academy.largesystems.eventhub.service;

import it.academy.largesystems.eventhub.config.SecurityUtil;
import it.academy.largesystems.eventhub.dto.TagRequestDTO;
import it.academy.largesystems.eventhub.dto.TagResponseDTO;
import it.academy.largesystems.eventhub.entity.Tag;
import it.academy.largesystems.eventhub.entity.User;
import it.academy.largesystems.eventhub.exception.ForbiddenException;
import it.academy.largesystems.eventhub.exception.ResourceConflictException;
import it.academy.largesystems.eventhub.exception.ResourceNotFoundException;
import it.academy.largesystems.eventhub.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final SecurityUtil securityUtil;

    private void checkAdminPermission() {
        User currentUser = securityUtil.getAuthenticatedUser();
        if (currentUser.getRole() == null || !"ROLE_ADMIN".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new ForbiddenException("Operazione consentita solo all'amministratore del sistema.");
        }
    }

    @Transactional
    public TagResponseDTO createTag(TagRequestDTO dto) {
        checkAdminPermission();

        String formattedName = dto.getName().toUpperCase().trim();
        if (tagRepository.existsByName(formattedName)) {
            throw new ResourceConflictException("Il tag #" + formattedName + " esiste già.");
        }

        Tag tag = new Tag();
        tag.setName(formattedName);
        Tag savedTag = tagRepository.save(tag);

        return new TagResponseDTO(savedTag.getId(), savedTag.getName());
    }

    @Transactional(readOnly = true)
    public List<TagResponseDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponseDTO> responseList = new ArrayList<>();

        for (Tag t : tags) {
            responseList.add(new TagResponseDTO(t.getId(), t.getName()));
        }
        return responseList;
    }

    @Transactional
    public TagResponseDTO updateTag(Long id, TagRequestDTO dto) {
        checkAdminPermission();

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag non trovato con ID: " + id));

        String formattedName = dto.getName().toUpperCase().trim();
        if (!tag.getName().equals(formattedName) && tagRepository.existsByName(formattedName)) {
            throw new ResourceConflictException("Esiste già un altro tag con il nome #" + formattedName);
        }

        tag.setName(formattedName);
        Tag updated = tagRepository.save(tag);

        return new TagResponseDTO(updated.getId(), updated.getName());
    }

    @Transactional
    public void deleteTag(Long id) {
        checkAdminPermission();

        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag non trovato con ID: " + id);
        }
        tagRepository.deleteById(id);
    }
}
