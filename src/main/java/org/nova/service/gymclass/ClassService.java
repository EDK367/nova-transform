package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.Class;

import java.util.List;
import java.util.Optional;

public interface ClassService {

    List<Class> findAll();

    Optional<Class> findById(Long id);

    Class create(Class gymClass);

    Class update(Long id, Class gymClass);

    void delete(Long id);
}