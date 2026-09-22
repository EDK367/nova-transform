package org.nova.service.member;

import org.nova.domain.model.member.MemberGoal;

import java.util.List;
import java.util.Optional;

public interface MemberGoalService {

    List<MemberGoal> findAll();

    Optional<MemberGoal> findById(Long id);

    MemberGoal create(MemberGoal memberGoal);

    MemberGoal update(Long id, MemberGoal memberGoal);

    void delete(Long id);
}