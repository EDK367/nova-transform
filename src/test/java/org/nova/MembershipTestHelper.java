package org.nova;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.repository.MemberRepository;
import org.nova.repository.MembershipFreezeRepository;
import org.nova.repository.MembershipHistoryRepository;
import org.nova.repository.MembershipPlanRepository;
import org.nova.repository.MembershipRepository;

import java.time.LocalDate;
import java.util.UUID;

@ApplicationScoped
public class MembershipTestHelper {

    @Inject
    MembershipRepository repository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    MembershipPlanRepository planRepository;

    @Inject
    MembershipHistoryRepository historyRepository;

    @Inject
    MembershipFreezeRepository freezeRepository;

    @Transactional
    public Long createActiveMembership(UUID memberId, Long planId) {
        Member member = memberRepository.findById(memberId);
        MembershipPlan plan = planRepository.findById(planId);
        Membership m = new Membership();
        m.setIdMember(member);
        m.setIdPlan(plan);
        m.setStatus(MembershipStatus.ACTIVE);
        m.setStartDate(LocalDate.now());
        m.setDueDate(LocalDate.now().plusMonths(6));
        repository.persist(m);
        return m.getId();
    }

    @Transactional
    public void deleteMembership(Long id) {
        freezeRepository.delete("idMembership.id", id);
        historyRepository.delete("idMembership.id", id);
        repository.deleteById(id);
    }
}
