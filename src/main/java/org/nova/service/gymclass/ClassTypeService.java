package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassType;
import org.nova.web.dto.request.gymclass.CreateClassTypeRequest;
import org.nova.web.dto.request.gymclass.UpdateClassTypeRequest;
import org.nova.web.dto.response.gymclass.ClassTypeResponse;

import java.util.List;
import java.util.Optional;

public interface ClassTypeService {

    List<ClassType> findAll();

    Optional<ClassType> findById(Long id);

    ClassType create(ClassType classType);

    ClassType update(Long id, ClassType classType);

    void delete(Long id);

    List<ClassTypeResponse> findAllResponses();

    ClassTypeResponse findResponseById(Long id);

    ClassTypeResponse create(CreateClassTypeRequest request);

    ClassTypeResponse update(Long id, UpdateClassTypeRequest request);
}