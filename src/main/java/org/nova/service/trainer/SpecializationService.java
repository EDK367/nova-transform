package org.nova.service.trainer;

import org.nova.domain.model.trainer.Specialization;

import java.util.List;
import java.util.Optional;

public interface SpecializationService {

    List<Specialization> findAll();

    Optional<Specialization> findById(Long id);

    Specialization create(Specialization specialization);

    Specialization update(Long id, Specialization specialization);

    void delete(Long id);
}