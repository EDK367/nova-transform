package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.enums.EnrollmentStatus;
import org.nova.domain.model.gymclass.ClassEnrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClassEnrollmentRepository implements PanacheRepositoryBase<ClassEnrollment, Long> {

    public Optional<ClassEnrollment> findByIdWithDetails(Long id) {
        return find("select e from ClassEnrollment e " +
                "join fetch e.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch c.idBranch b " +
                "join fetch e.idMember m " +
                "join fetch m.users u " +
                "where e.id = ?1", id).firstResultOptional();
    }

    public List<ClassEnrollment> findByClassId(Long classId) {
        return find("select e from ClassEnrollment e " +
                "join fetch e.idMember m " +
                "join fetch m.users u " +
                "where e.idClass.id = ?1 order by e.enrollmentDate asc", classId).list();
    }

    public List<ClassEnrollment> findByMemberId(UUID memberId) {
        return find("select e from ClassEnrollment e " +
                "join fetch e.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch c.idBranch b " +
                "where e.idMember.id = ?1 order by c.schedule desc", memberId).list();
    }

    public Optional<ClassEnrollment> findActiveEnrollment(Long classId, UUID memberId) {
        return find("idClass.id = ?1 and idMember.id = ?2 and status = ?3",
                classId, memberId, EnrollmentStatus.ENROLLED).firstResultOptional();
    }

    public Optional<ClassEnrollment> findByClassIdAndMemberId(Long classId, UUID memberId) {
        return find("idClass.id = ?1 and idMember.id = ?2", classId, memberId).firstResultOptional();
    }

    public long countEnrolledByClassId(Long classId) {
        return count("idClass.id = ?1 and status = ?2", classId, EnrollmentStatus.ENROLLED);
    }
}
