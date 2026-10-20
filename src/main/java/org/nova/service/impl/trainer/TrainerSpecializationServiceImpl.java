package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.Specialization;
import org.nova.domain.model.trainer.Trainer;
import org.nova.domain.model.trainer.TrainerSpecialization;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.SpecializationRepository;
import org.nova.repository.TrainerRepository;
import org.nova.repository.TrainerSpecializationRepository;
import org.nova.service.trainer.TrainerSpecializationService;
import org.nova.web.dto.request.trainer.AssignTrainerSpecializationRequest;
import org.nova.web.dto.response.trainer.TrainerSpecializationResponse;
import org.nova.web.mapper.TrainerSpecializationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerSpecializationServiceImpl implements TrainerSpecializationService {

    private final TrainerSpecializationRepository repository;
    private final TrainerRepository trainerRepository;
    private final SpecializationRepository specializationRepository;
    private final TrainerSpecializationMapper mapper;

    @Override
    public List<TrainerSpecialization> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<TrainerSpecializationResponse> findAllResponses(UUID trainerId) {
        List<TrainerSpecialization> list = trainerId != null
                ? repository.findByTrainerId(trainerId)
                : repository.listAllWithDetails();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<TrainerSpecialization> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public TrainerSpecializationResponse findResponseById(Long id) {
        TrainerSpecialization ts = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Especializacion de entrenador no encontrada con ID: " + id));
        return mapper.toResponse(ts);
    }

    @Override
    @Transactional
    public TrainerSpecialization create(TrainerSpecialization trainerSpecialization) {
        repository.persist(trainerSpecialization);
        return trainerSpecialization;
    }

    @Override
    @Transactional
    public TrainerSpecializationResponse assignSpecialization(AssignTrainerSpecializationRequest request) {
        Trainer trainer = trainerRepository.findByIdWithUser(request.trainerId())
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + request.trainerId()));

        Specialization specialization = specializationRepository.findByIdOptional(request.specializationId())
                .orElseThrow(() -> new NotFoundException("Especializacion no encontrada con ID: " + request.specializationId()));

        if (repository.existsByTrainerAndSpecialization(request.trainerId(), request.specializationId())) {
            throw new ConflictException("El entrenador ya tiene asignada esta especializacion");
        }

        TrainerSpecialization ts = new TrainerSpecialization();
        ts.setIdTrainer(trainer);
        ts.setIdSpecialization(specialization);

        repository.persist(ts);
        return mapper.toResponse(ts);
    }

    @Override
    @Transactional
    public TrainerSpecialization update(Long id, TrainerSpecialization trainerSpecialization) {
        TrainerSpecialization existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setIdTrainer(trainerSpecialization.getIdTrainer());
        existing.setIdSpecialization(trainerSpecialization.getIdSpecialization());
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteTrainerSpecialization(Long id) {
        TrainerSpecialization ts = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Asignacion de especializacion no encontrada con ID: " + id));
        repository.delete(ts);
    }
}
