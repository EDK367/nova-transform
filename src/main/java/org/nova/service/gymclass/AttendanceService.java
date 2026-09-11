package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    List<Attendance> findAll();

    Optional<Attendance> findById(Long id);

    Attendance create(Attendance attendance);

    Attendance update(Long id, Attendance attendance);

    void delete(Long id);
}