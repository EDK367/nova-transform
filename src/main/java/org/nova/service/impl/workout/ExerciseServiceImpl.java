package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.Exercise;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.ExerciseRepository;
import org.nova.service.workout.ExerciseService;
import org.nova.web.dto.request.workout.CreateExerciseRequest;
import org.nova.web.dto.request.workout.UpdateExerciseRequest;
import org.nova.web.dto.response.workout.ExerciseResponse;
import org.nova.web.mapper.ExerciseMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository repository;
    private final ExerciseMapper mapper;

    @Override
    public List<Exercise> findAll() {
        return repository.listAll();
    }

    @Override
    public List<ExerciseResponse> findAllResponses(String muscleGroup) {
        List<Exercise> list = (muscleGroup != null && !muscleGroup.isBlank())
                ? repository.findByMuscleGroup(muscleGroup.trim())
                : repository.listAll();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<Exercise> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public ExerciseResponse findResponseById(Long id) {
        Exercise exercise = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio no encontrado con ID: " + id));
        return mapper.toResponse(exercise);
    }

    @Override
    @Transactional
    public Exercise create(Exercise exercise) {
        repository.persist(exercise);
        return exercise;
    }

    @Override
    @Transactional
    public ExerciseResponse createExercise(CreateExerciseRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un ejercicio con el nombre: " + request.name().trim());
        }

        Exercise exercise = new Exercise();
        exercise.setName(request.name().trim());
        exercise.setDescription(request.description() != null ? request.description().trim() : null);
        exercise.setMuscleGroup(request.muscleGroup().trim());

        repository.persist(exercise);
        return mapper.toResponse(exercise);
    }

    @Override
    @Transactional
    public Exercise update(Long id, Exercise exercise) {
        Exercise existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(exercise.getName());
        existing.setDescription(exercise.getDescription());
        existing.setMuscleGroup(exercise.getMuscleGroup());
        return existing;
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(Long id, UpdateExerciseRequest request) {
        Exercise exercise = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otro ejercicio con el nombre: " + request.name().trim());
        }

        exercise.setName(request.name().trim());
        exercise.setDescription(request.description() != null ? request.description().trim() : null);
        exercise.setMuscleGroup(request.muscleGroup().trim());

        return mapper.toResponse(exercise);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio no encontrado con ID: " + id));
        repository.delete(exercise);
    }
}
