package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.TrainerSpecialization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TrainerSpecializationRepository implements PanacheRepositoryBase<TrainerSpecialization, Long> {

    public boolean existsByTrainerAndSpecialization(UUID trainerId, Long specializationId) {
        return count("idTrainer.id = ?1 and idSpecialization.id = ?2", trainerId, specializationId) > 0;
    }

    public List<TrainerSpecialization> findByTrainerId(UUID trainerId) {
        return find("select ts from TrainerSpecialization ts left join fetch ts.idTrainer t left join fetch t.users left join fetch ts.idSpecialization where ts.idTrainer.id = ?1", trainerId).list();
    }

    public List<TrainerSpecialization> listAllWithDetails() {
        return find("select ts from TrainerSpecialization ts left join fetch ts.idTrainer t left join fetch t.users left join fetch ts.idSpecialization").list();
    }

    public Optional<TrainerSpecialization> findByIdWithDetails(Long id) {
        return find("select ts from TrainerSpecialization ts left join fetch ts.idTrainer t left join fetch t.users left join fetch ts.idSpecialization where ts.id = ?1", id).firstResultOptional();
    }
}
