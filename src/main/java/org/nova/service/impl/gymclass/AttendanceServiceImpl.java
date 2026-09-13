package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.repository.AttendanceRepository;
import org.nova.service.gymclass.AttendanceService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.listAll();
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Attendance create(Attendance attendance) {
        attendanceRepository.persist(attendance);
        return attendance;
    }

    @Override
    @Transactional
    public Attendance update(Long id, Attendance attendance) {
        Attendance existing = attendanceRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdBranch(attendance.getIdBranch());
        existing.setIdReceptionist(attendance.getIdReceptionist());
        existing.setIdMember(attendance.getIdMember());
        existing.setDate(attendance.getDate());
        existing.setEntryTime(attendance.getEntryTime());
        existing.setExitTime(attendance.getExitTime());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }
}