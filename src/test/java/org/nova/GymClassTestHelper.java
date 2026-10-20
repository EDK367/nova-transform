package org.nova;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.nova.domain.enums.ClassStatus;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.gymclass.Class;
import org.nova.domain.model.gymclass.ClassType;
import org.nova.domain.model.trainer.Trainer;
import org.nova.repository.*;

import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class GymClassTestHelper {

    @Inject
    ClassRepository classRepository;

    @Inject
    ClassTypeRepository classTypeRepository;

    @Inject
    ClassEnrollmentRepository enrollmentRepository;

    @Inject
    WaitlistRepository waitlistRepository;

    @Inject
    ClassRatingRepository ratingRepository;

    @Inject
    AttendanceRepository attendanceRepository;

    @Inject
    BranchRepository branchRepository;

    @Inject
    TrainerRepository trainerRepository;

    @Transactional
    public Long createTestClass(Long branchId, Long classTypeId, UUID trainerId, long maxCapacity) {
        Branch branch = branchRepository.findById(branchId);
        ClassType classType = classTypeRepository.findById(classTypeId);
        Trainer trainer = trainerRepository.findById(trainerId);

        Class c = new Class();
        c.setIdBranch(branch);
        c.setIdClassType(classType);
        c.setIdTrainer(trainer);
        c.setStatus(ClassStatus.SCHEDULED);
        c.setSchedule(Instant.now().plusSeconds(86400));
        c.setDuration(60L);
        c.setMaxCapacity(maxCapacity);

        classRepository.persist(c);
        return c.getId();
    }

    @Transactional
    public void deleteClassAndRelated(Long classId) {
        ratingRepository.delete("idEnrollment.idClass.id", classId);
        enrollmentRepository.delete("idClass.id", classId);
        waitlistRepository.delete("idClass.id", classId);
        classRepository.deleteById(classId);
    }

    @Transactional
    public void deleteAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
}
