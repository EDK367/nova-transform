package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassType;

import java.util.List;
import java.util.Optional;

public interface ClassTypeService {

    List<ClassType> findAll();

    Optional<ClassType> findById(Long id);

    ClassType create(ClassType classType);

    ClassType update(Long id, ClassType classType);

    void delete(Long id);
}