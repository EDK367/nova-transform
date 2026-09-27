package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.ClassRating;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClassRatingRepository implements PanacheRepositoryBase<ClassRating, Long> {

    public Optional<ClassRating> findByIdWithDetails(Long id) {
        return find("select r from ClassRating r " +
                "join fetch r.idEnrollment e " +
                "join fetch e.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch e.idMember m " +
                "join fetch m.users u " +
                "where r.id = ?1", id).firstResultOptional();
    }

    public List<ClassRating> findByClassId(Long classId) {
        return find("select r from ClassRating r " +
                "join fetch r.idEnrollment e " +
                "join fetch e.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch e.idMember m " +
                "join fetch m.users u " +
                "where c.id = ?1 order by r.dateTime desc", classId).list();
    }

    public Optional<ClassRating> findByEnrollmentId(Long enrollmentId) {
        return find("idEnrollment.id", enrollmentId).firstResultOptional();
    }

    public boolean existsByEnrollmentId(Long enrollmentId) {
        return count("idEnrollment.id", enrollmentId) > 0;
    }

    public Double calculateAverageRatingByClassId(Long classId) {
        Double avg = getEntityManager().createQuery(
                "select avg(r.rating) from ClassRating r where r.idEnrollment.idClass.id = :classId",
                Double.class)
                .setParameter("classId", classId)
                .getSingleResult();
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }
}
