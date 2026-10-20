package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.workout.RoutineExercise;
import org.nova.domain.model.workout.WorkoutLog;
import org.nova.exception.NotFoundException;
import org.nova.repository.MemberRepository;
import org.nova.repository.RoutineExerciseRepository;
import org.nova.repository.WorkoutLogRepository;
import org.nova.service.workout.WorkoutLogService;
import org.nova.web.dto.request.workout.CreateWorkoutLogRequest;
import org.nova.web.dto.request.workout.UpdateWorkoutLogRequest;
import org.nova.web.dto.response.workout.WorkoutLogResponse;
import org.nova.web.mapper.WorkoutLogMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutLogRepository repository;
    private final MemberRepository memberRepository;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final WorkoutLogMapper mapper;

    @Override
    public List<WorkoutLog> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<WorkoutLogResponse> findAllResponses(UUID memberId, Long routineExerciseId) {
        List<WorkoutLog> list;
        if (memberId != null) {
            list = repository.findByMemberId(memberId);
        } else if (routineExerciseId != null) {
            list = repository.findByRoutineExerciseId(routineExerciseId);
        } else {
            list = repository.listAllWithDetails();
        }
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<WorkoutLog> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public WorkoutLogResponse findResponseById(Long id) {
        WorkoutLog wl = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro de entrenamiento no encontrado con ID: " + id));
        return mapper.toResponse(wl);
    }

    @Override
    @Transactional
    public WorkoutLog create(WorkoutLog workoutLog) {
        repository.persist(workoutLog);
        return workoutLog;
    }

    @Override
    @Transactional
    public WorkoutLogResponse createWorkoutLog(CreateWorkoutLogRequest request) {
        Member member = memberRepository.findByIdWithUser(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        RoutineExercise routineExercise = routineExerciseRepository.findByIdWithDetails(request.routineExerciseId())
                .orElseThrow(() -> new NotFoundException("Ejercicio de rutina no encontrado con ID: " + request.routineExerciseId()));

        WorkoutLog wl = new WorkoutLog();
        wl.setIdMember(member);
        wl.setIdRoutineExercise(routineExercise);
        wl.setDate(request.date() != null ? request.date() : Instant.now());
        wl.setActualSets(request.actualSets());
        wl.setActualReps(request.actualReps());
        wl.setActualWeight(request.actualWeight());
        wl.setObservation(request.observation() != null ? request.observation().trim() : null);

        repository.persist(wl);
        return mapper.toResponse(wl);
    }

    @Override
    @Transactional
    public WorkoutLog update(Long id, WorkoutLog workoutLog) {
        WorkoutLog existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setIdMember(workoutLog.getIdMember());
        existing.setIdRoutineExercise(workoutLog.getIdRoutineExercise());
        existing.setDate(workoutLog.getDate());
        existing.setActualSets(workoutLog.getActualSets());
        existing.setActualReps(workoutLog.getActualReps());
        existing.setActualWeight(workoutLog.getActualWeight());
        existing.setObservation(workoutLog.getObservation());
        return existing;
    }

    @Override
    @Transactional
    public WorkoutLogResponse updateWorkoutLog(Long id, UpdateWorkoutLogRequest request) {
        WorkoutLog wl = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro de entrenamiento no encontrado con ID: " + id));

        if (request.date() != null) {
            wl.setDate(request.date());
        }
        if (request.actualSets() != null) {
            wl.setActualSets(request.actualSets());
        }
        if (request.actualReps() != null) {
            wl.setActualReps(request.actualReps());
        }
        if (request.actualWeight() != null) {
            wl.setActualWeight(request.actualWeight());
        }
        if (request.observation() != null) {
            wl.setObservation(request.observation().trim());
        }

        return mapper.toResponse(wl);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteWorkoutLog(Long id) {
        WorkoutLog wl = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Registro de entrenamiento no encontrado con ID: " + id));
        repository.delete(wl);
    }
}
