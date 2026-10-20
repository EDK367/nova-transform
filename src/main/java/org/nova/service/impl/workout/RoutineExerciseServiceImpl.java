package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.Exercise;
import org.nova.domain.model.workout.Routine;
import org.nova.domain.model.workout.RoutineExercise;
import org.nova.exception.NotFoundException;
import org.nova.repository.ExerciseRepository;
import org.nova.repository.RoutineExerciseRepository;
import org.nova.repository.RoutineRepository;
import org.nova.service.workout.RoutineExerciseService;
import org.nova.web.dto.request.workout.CreateRoutineExerciseRequest;
import org.nova.web.dto.request.workout.UpdateRoutineExerciseRequest;
import org.nova.web.dto.response.workout.RoutineExerciseResponse;
import org.nova.web.mapper.RoutineExerciseMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class RoutineExerciseServiceImpl implements RoutineExerciseService {

    private final RoutineExerciseRepository repository;
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineExerciseMapper mapper;

    @Override
    public List<RoutineExercise> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<RoutineExerciseResponse> findAllResponses(Long routineId, String trainingDay) {
        List<RoutineExercise> list;
        if (routineId != null) {
            list = repository.findByRoutineId(routineId);
        } else if (trainingDay != null && !trainingDay.isBlank()) {
            list = repository.findByTrainingDay(trainingDay.trim());
        } else {
            list = repository.listAllWithDetails();
        }
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<RoutineExercise> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public RoutineExerciseResponse findResponseById(Long id) {
        RoutineExercise re = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio de rutina no encontrado con ID: " + id));
        return mapper.toResponse(re);
    }

    @Override
    @Transactional
    public RoutineExercise create(RoutineExercise routineExercise) {
        repository.persist(routineExercise);
        return routineExercise;
    }

    @Override
    @Transactional
    public RoutineExerciseResponse createRoutineExercise(CreateRoutineExerciseRequest request) {
        Routine routine = routineRepository.findByIdOptional(request.routineId())
                .orElseThrow(() -> new NotFoundException("Rutina no encontrada con ID: " + request.routineId()));

        Exercise exercise = exerciseRepository.findByIdOptional(request.exerciseId())
                .orElseThrow(() -> new NotFoundException("Ejercicio no encontrado con ID: " + request.exerciseId()));

        RoutineExercise re = new RoutineExercise();
        re.setIdRoutine(routine);
        re.setIdExercise(exercise);
        re.setTrainingDay(request.trainingDay().trim().toUpperCase());
        re.setSets(request.sets());
        re.setReps(request.reps());
        re.setWeight(request.weight());
        re.setRest(request.rest());

        repository.persist(re);
        return mapper.toResponse(re);
    }

    @Override
    @Transactional
    public RoutineExercise update(Long id, RoutineExercise routineExercise) {
        RoutineExercise existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setIdRoutine(routineExercise.getIdRoutine());
        existing.setIdExercise(routineExercise.getIdExercise());
        existing.setTrainingDay(routineExercise.getTrainingDay());
        existing.setSets(routineExercise.getSets());
        existing.setReps(routineExercise.getReps());
        existing.setWeight(routineExercise.getWeight());
        existing.setRest(routineExercise.getRest());
        return existing;
    }

    @Override
    @Transactional
    public RoutineExerciseResponse updateRoutineExercise(Long id, UpdateRoutineExerciseRequest request) {
        RoutineExercise re = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio de rutina no encontrado con ID: " + id));

        if (request.trainingDay() != null && !request.trainingDay().isBlank()) {
            re.setTrainingDay(request.trainingDay().trim().toUpperCase());
        }
        if (request.sets() != null) {
            re.setSets(request.sets());
        }
        if (request.reps() != null) {
            re.setReps(request.reps());
        }
        if (request.weight() != null) {
            re.setWeight(request.weight());
        }
        if (request.rest() != null) {
            re.setRest(request.rest());
        }

        return mapper.toResponse(re);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteRoutineExercise(Long id) {
        RoutineExercise re = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Ejercicio de rutina no encontrado con ID: " + id));
        repository.delete(re);
    }
}
