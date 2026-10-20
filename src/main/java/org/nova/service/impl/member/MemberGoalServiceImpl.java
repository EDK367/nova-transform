package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Goal;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.member.MemberGoal;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.GoalRepository;
import org.nova.repository.MemberGoalRepository;
import org.nova.repository.MemberRepository;
import org.nova.service.member.MemberGoalService;
import org.nova.web.dto.request.member.AssignMemberGoalRequest;
import org.nova.web.dto.response.member.MemberGoalResponse;
import org.nova.web.mapper.MemberGoalMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class MemberGoalServiceImpl implements MemberGoalService {

    private final MemberGoalRepository repository;
    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final MemberGoalMapper mapper;

    @Override
    public List<MemberGoal> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<MemberGoalResponse> findAllResponses(UUID memberId) {
        List<MemberGoal> list = memberId != null
                ? repository.findByMemberId(memberId)
                : repository.listAllWithDetails();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<MemberGoal> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public MemberGoalResponse findResponseById(Long id) {
        MemberGoal mg = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Meta de socio no encontrada con ID: " + id));
        return mapper.toResponse(mg);
    }

    @Override
    @Transactional
    public MemberGoal create(MemberGoal memberGoal) {
        repository.persist(memberGoal);
        return memberGoal;
    }

    @Override
    @Transactional
    public MemberGoalResponse assignGoal(AssignMemberGoalRequest request) {
        Member member = memberRepository.findByIdWithUser(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        Goal goal = goalRepository.findByIdOptional(request.goalId())
                .orElseThrow(() -> new NotFoundException("Objetivo no encontrado con ID: " + request.goalId()));

        if (repository.existsByMemberAndGoal(request.memberId(), request.goalId())) {
            throw new ConflictException("El socio ya tiene asignado este objetivo");
        }

        MemberGoal mg = new MemberGoal();
        mg.setIdMember(member);
        mg.setIdGoal(goal);

        repository.persist(mg);
        return mapper.toResponse(mg);
    }

    @Override
    @Transactional
    public MemberGoal update(Long id, MemberGoal memberGoal) {
        MemberGoal existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setIdMember(memberGoal.getIdMember());
        existing.setIdGoal(memberGoal.getIdGoal());
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteMemberGoal(Long id) {
        MemberGoal mg = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Meta de socio no encontrada con ID: " + id));
        repository.delete(mg);
    }
}
