package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.Waitlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class WaitlistRepository implements PanacheRepositoryBase<Waitlist, Long> {

    public Optional<Waitlist> findByIdWithDetails(Long id) {
        return find("select w from Waitlist w " +
                "join fetch w.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch w.idMember m " +
                "join fetch m.users u " +
                "where w.id = ?1", id).firstResultOptional();
    }

    public List<Waitlist> findByClassId(Long classId) {
        return find("select w from Waitlist w " +
                "join fetch w.idMember m " +
                "join fetch m.users u " +
                "where w.idClass.id = ?1 order by w.registrationDate asc", classId).list();
    }

    public List<Waitlist> findByMemberId(UUID memberId) {
        return find("select w from Waitlist w " +
                "join fetch w.idClass c " +
                "join fetch c.idClassType ct " +
                "join fetch c.idBranch b " +
                "where w.idMember.id = ?1 order by w.registrationDate desc", memberId).list();
    }

    public Optional<Waitlist> findFirstInWaitlist(Long classId) {
        return find("select w from Waitlist w " +
                "join fetch w.idClass c " +
                "join fetch w.idMember m " +
                "join fetch m.users u " +
                "where w.idClass.id = ?1 order by w.registrationDate asc", classId).firstResultOptional();
    }

    public long countByClassId(Long classId) {
        return count("idClass.id", classId);
    }

    public boolean existsByClassIdAndMemberId(Long classId, UUID memberId) {
        return count("idClass.id = ?1 and idMember.id = ?2", classId, memberId) > 0;
    }

    public void deleteByClassIdAndMemberId(Long classId, UUID memberId) {
        delete("idClass.id = ?1 and idMember.id = ?2", classId, memberId);
    }
}