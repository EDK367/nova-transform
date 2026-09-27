package org.nova.service.gymclass;

import org.nova.common.PageResponse;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.web.dto.request.gymclass.CreateAttendanceRequest;
import org.nova.web.dto.request.gymclass.UpdateAttendanceExitRequest;
import org.nova.web.dto.response.gymclass.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceService {

    List<Attendance> findAll();

    Optional<Attendance> findById(Long id);

    Attendance create(Attendance attendance);

    Attendance update(Long id, Attendance attendance);

    void delete(Long id);

    AttendanceResponse checkIn(CreateAttendanceRequest request, String authenticatedUsername);

    AttendanceResponse checkOut(Long id, UpdateAttendanceExitRequest request);

    PageResponse<AttendanceResponse> findPaged(
            int page,
            int size,
            Long branchId,
            UUID memberId,
            LocalDate date,
            String sortBy,
            String sortDirection
    );

    List<AttendanceResponse> findByMemberId(UUID memberId);

    AttendanceResponse findResponseById(Long id);
}