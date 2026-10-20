package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.MemberGoal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MemberGoalRepository implements PanacheRepositoryBase<MemberGoal, Long> {

    public boolean existsByMemberAndGoal(UUID memberId, Long goalId) {
        return count("idMember.id = ?1 and idGoal.id = ?2", memberId, goalId) > 0;
    }

    public List<MemberGoal> listAllWithDetails() {
        return find("select mg from MemberGoal mg left join fetch mg.idMember m left join fetch m.users left join fetch mg.idGoal").list();
    }

    public Optional<MemberGoal> findByIdWithDetails(Long id) {
        return find("select mg from MemberGoal mg left join fetch mg.idMember m left join fetch m.users left join fetch mg.idGoal where mg.id = ?1", id).firstResultOptional();
    }

    public List<MemberGoal> findByMemberId(UUID memberId) {
        return find("select mg from MemberGoal mg left join fetch mg.idMember m left join fetch m.users left join fetch mg.idGoal where mg.idMember.id = ?1", memberId).list();
    }
}
