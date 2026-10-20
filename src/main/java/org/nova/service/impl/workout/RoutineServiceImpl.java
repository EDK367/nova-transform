package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.domain.model.workout.Routine;
import org.nova.exception.NotFoundException;
import org.nova.repository.RoutineRepository;
import org.nova.repository.TrainerAssignmentRepository;
import org.nova.service.workout.RoutineService;
import org.nova.web.dto.request.workout.CreateRoutineRequest;
import org.nova.web.dto.request.workout.UpdateRoutineRequest;
import org.nova.web.dto.response.workout.RoutineResponse;
import org.nova.web.mapper.RoutineMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository repository;
    private final TrainerAssignmentRepository assignmentRepository;
    private final RoutineMapper mapper;

    @Override
    public List<Routine> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<RoutineResponse> findAllResponses(Long assignmentId, UUID memberId) {
        List<Routine> list;
        if (assignmentId != null) {
            list = repository.findByAssignmentId(assignmentId);
        } else if (memberId != null) {
            list = repository.findByMemberId(memberId);
        } else {
            list = repository.listAllWithDetails();
        }
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<Routine> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public RoutineResponse findResponseById(Long id) {
        Routine routine = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Rutina no encontrada con ID: " + id));
        return mapper.toResponse(routine);
    }

    @Override
    @Transactional
    public Routine create(Routine routine) {
        repository.persist(routine);
        return routine;
    }

    @Override
    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest request) {
        TrainerAssignment assignment = assignmentRepository.findByIdWithDetails(request.assignmentId())
                .orElseThrow(() -> new NotFoundException("Asignacion de entrenador no encontrada con ID: " + request.assignmentId()));

        Routine routine = new Routine();
        routine.setIdAssignment(assignment);
        routine.setName(request.name().trim());
        routine.setDescription(request.description() != null ? request.description().trim() : null);
        routine.setActive(true);

        repository.persist(routine);
        return mapper.toResponse(routine);
    }

    @Override
    @Transactional
    public Routine update(Long id, Routine routine) {
        Routine existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(routine.getName());
        existing.setDescription(routine.getDescription());
        existing.setActive(routine.getActive());
        return existing;
    }

    @Override
    @Transactional
    public RoutineResponse updateRoutine(Long id, UpdateRoutineRequest request) {
        Routine routine = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Rutina no encontrada con ID: " + id));

        routine.setName(request.name().trim());
        routine.setDescription(request.description() != null ? request.description().trim() : null);
        if (request.active() != null) {
            routine.setActive(request.active());
        }

        return mapper.toResponse(routine);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteRoutine(Long id) {
        Routine routine = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Rutina no encontrada con ID: " + id));
        repository.delete(routine);
    }
}
