package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.TrainerAssignment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TrainerAssignmentRepository implements PanacheRepositoryBase<TrainerAssignment, Long> {

    public List<TrainerAssignment> listAllWithDetails() {
        return find("select ta from TrainerAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users").list();
    }

    public Optional<TrainerAssignment> findByIdWithDetails(Long id) {
        return find("select ta from TrainerAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where ta.id = ?1", id).firstResultOptional();
    }

    public List<TrainerAssignment> findByMemberId(UUID memberId) {
        return find("select ta from TrainerAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where ta.idMember.id = ?1", memberId).list();
    }

    public List<TrainerAssignment> findByTrainerId(UUID trainerId) {
        return find("select ta from TrainerAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where ta.idTrainer.id = ?1", trainerId).list();
    }

    public List<TrainerAssignment> findActiveByMemberId(UUID memberId) {
        return find("select ta from TrainerAssignment ta left join fetch ta.idMember m left join fetch m.users left join fetch ta.idTrainer t left join fetch t.users where ta.idMember.id = ?1 and ta.active = true", memberId).list();
    }
}
