package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.WorkoutLog;

@ApplicationScoped
public class WorkoutLogRepository implements PanacheRepositoryBase<WorkoutLog, Long> {
}