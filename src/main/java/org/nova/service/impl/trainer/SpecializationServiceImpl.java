package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.Specialization;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.SpecializationRepository;
import org.nova.service.trainer.SpecializationService;
import org.nova.web.dto.request.trainer.CreateSpecializationRequest;
import org.nova.web.dto.request.trainer.UpdateSpecializationRequest;
import org.nova.web.dto.response.trainer.SpecializationResponse;
import org.nova.web.mapper.SpecializationMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final SpecializationMapper mapper;

    @Override
    public List<Specialization> findAll() {
        return repository.listAll();
    }

    @Override
    public List<SpecializationResponse> findAllResponses() {
        return mapper.toResponseList(repository.listAll());
    }

    @Override
    public Optional<Specialization> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public SpecializationResponse findResponseById(Long id) {
        Specialization specialization = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Especializacion no encontrada con ID: " + id));
        return mapper.toResponse(specialization);
    }

    @Override
    @Transactional
    public Specialization create(Specialization specialization) {
        repository.persist(specialization);
        return specialization;
    }

    @Override
    @Transactional
    public SpecializationResponse createSpecialization(CreateSpecializationRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe una especializacion con el nombre: " + request.name().trim());
        }

        Specialization specialization = new Specialization();
        specialization.setName(request.name().trim());
        specialization.setDescription(request.description() != null ? request.description().trim() : null);

        repository.persist(specialization);
        return mapper.toResponse(specialization);
    }

    @Override
    @Transactional
    public Specialization update(Long id, Specialization specialization) {
        Specialization existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(specialization.getName());
        existing.setDescription(specialization.getDescription());
        return existing;
    }

    @Override
    @Transactional
    public SpecializationResponse updateSpecialization(Long id, UpdateSpecializationRequest request) {
        Specialization specialization = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Especializacion no encontrada con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otra especializacion con el nombre: " + request.name().trim());
        }

        specialization.setName(request.name().trim());
        specialization.setDescription(request.description() != null ? request.description().trim() : null);

        return mapper.toResponse(specialization);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteSpecialization(Long id) {
        Specialization specialization = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Especializacion no encontrada con ID: " + id));
        repository.delete(specialization);
    }
}
