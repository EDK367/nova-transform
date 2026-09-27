package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.ClassEnrollment;
import org.nova.web.dto.request.gymclass.CreateEnrollmentRequest;
import org.nova.web.dto.response.gymclass.ClassEnrollmentResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassEnrollmentService {

    List<ClassEnrollment> findAll();

    Optional<ClassEnrollment> findById(Long id);

    ClassEnrollment create(ClassEnrollment classEnrollment);

    ClassEnrollment update(Long id, ClassEnrollment classEnrollment);

    void delete(Long id);

    ClassEnrollmentResponse enroll(CreateEnrollmentRequest request, String authenticatedUsername);

    ClassEnrollmentResponse cancelEnrollment(Long id, String authenticatedUsername);

    List<ClassEnrollmentResponse> findByMemberId(UUID memberId);

    List<ClassEnrollmentResponse> findByClassId(Long classId);

    ClassEnrollmentResponse findResponseById(Long id);
}