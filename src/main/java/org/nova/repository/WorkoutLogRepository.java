package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.WorkoutLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class WorkoutLogRepository implements PanacheRepositoryBase<WorkoutLog, Long> {

    public List<WorkoutLog> listAllWithDetails() {
        return find("select wl from WorkoutLog wl left join fetch wl.idMember m left join fetch m.users left join fetch wl.idRoutineExercise re left join fetch re.idExercise left join fetch re.idRoutine").list();
    }

    public Optional<WorkoutLog> findByIdWithDetails(Long id) {
        return find("select wl from WorkoutLog wl left join fetch wl.idMember m left join fetch m.users left join fetch wl.idRoutineExercise re left join fetch re.idExercise left join fetch re.idRoutine where wl.id = ?1", id).firstResultOptional();
    }

    public List<WorkoutLog> findByMemberId(UUID memberId) {
        return find("select wl from WorkoutLog wl left join fetch wl.idMember m left join fetch m.users left join fetch wl.idRoutineExercise re left join fetch re.idExercise left join fetch re.idRoutine where wl.idMember.id = ?1 order by wl.date desc", memberId).list();
    }

    public List<WorkoutLog> findByRoutineExerciseId(Long routineExerciseId) {
        return find("select wl from WorkoutLog wl left join fetch wl.idMember m left join fetch m.users left join fetch wl.idRoutineExercise re left join fetch re.idExercise left join fetch re.idRoutine where wl.idRoutineExercise.id = ?1 order by wl.date desc", routineExerciseId).list();
    }
}
