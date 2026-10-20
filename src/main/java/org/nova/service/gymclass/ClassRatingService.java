package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassRating;
import org.nova.web.dto.request.gymclass.CreateClassRatingRequest;
import org.nova.web.dto.response.gymclass.ClassRatingResponse;

import java.util.List;
import java.util.Optional;

public interface ClassRatingService {

    List<ClassRating> findAll();

    Optional<ClassRating> findById(Long id);

    ClassRating create(ClassRating classRating);

    ClassRating update(Long id, ClassRating classRating);

    void delete(Long id);

    ClassRatingResponse createRating(CreateClassRatingRequest request, String authenticatedUsername);

    List<ClassRatingResponse> findByClassId(Long classId);

    ClassRatingResponse findResponseById(Long id);

    Double getAverageRating(Long classId);
}