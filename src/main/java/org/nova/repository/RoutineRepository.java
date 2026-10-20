package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.Routine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoutineRepository implements PanacheRepositoryBase<Routine, Long> {

    public List<Routine> listAllWithDetails() {
        return find("select r from Routine r left join fetch r.idAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users").list();
    }

    public Optional<Routine> findByIdWithDetails(Long id) {
        return find("select r from Routine r left join fetch r.idAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where r.id = ?1", id).firstResultOptional();
    }

    public List<Routine> findByAssignmentId(Long assignmentId) {
        return find("select r from Routine r left join fetch r.idAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where r.idAssignment.id = ?1", assignmentId).list();
    }

    public List<Routine> findByMemberId(UUID memberId) {
        return find("select r from Routine r left join fetch r.idAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where ta.idMember.id = ?1", memberId).list();
    }
}
