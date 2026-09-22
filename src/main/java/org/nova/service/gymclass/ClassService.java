package org.nova.service.gymclass;

import java.util.List;
import java.util.Optional;

public interface ClassService {

    List<org.nova.domain.model.gymclass.Class> findAll();

    Optional<org.nova.domain.model.gymclass.Class> findById(Long id);

    org.nova.domain.model.gymclass.Class create(
            org.nova.domain.model.gymclass.Class gymClass
    );

    org.nova.domain.model.gymclass.Class update(
            Long id,
            org.nova.domain.model.gymclass.Class gymClass
    );

    void delete(Long id);
}