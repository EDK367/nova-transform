package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassEnrollment;

import java.util.List;
import java.util.Optional;

public interface ClassEnrollmentService {

    List<ClassEnrollment> findAll();

    Optional<ClassEnrollment> findById(Long id);

    ClassEnrollment create(ClassEnrollment classEnrollment);

    ClassEnrollment update(Long id, ClassEnrollment classEnrollment);

    void delete(Long id);
}