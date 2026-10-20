package org.nova.service.member;

import org.nova.domain.model.member.MemberGoal;
import org.nova.web.dto.request.member.AssignMemberGoalRequest;
import org.nova.web.dto.response.member.MemberGoalResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberGoalService {

    List<MemberGoal> findAll();

    List<MemberGoalResponse> findAllResponses(UUID memberId);

    Optional<MemberGoal> findById(Long id);

    MemberGoalResponse findResponseById(Long id);

    MemberGoal create(MemberGoal memberGoal);

    MemberGoalResponse assignGoal(AssignMemberGoalRequest request);

    MemberGoal update(Long id, MemberGoal memberGoal);

    void delete(Long id);

    void deleteMemberGoal(Long id);
}
