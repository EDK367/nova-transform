package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassRating;

import java.util.List;
import java.util.Optional;

public interface ClassRatingService {

    List<ClassRating> findAll();

    Optional<ClassRating> findById(Long id);

    ClassRating create(ClassRating classRating);

    ClassRating update(Long id, ClassRating classRating);

    void delete(Long id);
}