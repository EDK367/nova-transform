package org.nova.service.gymclass;

import org.nova.common.PageResponse;
import org.nova.domain.enums.ClassStatus;
import org.nova.domain.model.gymclass.Class;
import org.nova.web.dto.request.gymclass.CreateClassRequest;
import org.nova.web.dto.request.gymclass.UpdateClassRequest;
import org.nova.web.dto.response.gymclass.ClassResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassService {

    List<Class> findAll();

    Optional<Class> findById(Long id);

    Class create(Class gymClass);

    Class update(Long id, Class gymClass);

    void delete(Long id);

    PageResponse<ClassResponse> findPaged(
            int page,
            int size,
            Long branchId,
            Long classTypeId,
            UUID trainerId,
            ClassStatus status,
            Instant startDate,
            Instant endDate,
            String sortBy,
            String sortDirection
    );

    ClassResponse findResponseById(Long id);

    ClassResponse createClass(CreateClassRequest request);

    ClassResponse updateClass(Long id, UpdateClassRequest request);

    ClassResponse cancelClass(Long id);
}