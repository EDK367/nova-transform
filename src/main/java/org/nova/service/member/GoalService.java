package org.nova.service.member;

import org.nova.domain.model.member.Goal;
import org.nova.web.dto.request.member.CreateGoalRequest;
import org.nova.web.dto.request.member.UpdateGoalRequest;
import org.nova.web.dto.response.member.GoalResponse;

import java.util.List;
import java.util.Optional;

public interface GoalService {

    List<Goal> findAll();

    List<GoalResponse> findAllResponses();

    Optional<Goal> findById(Long id);

    GoalResponse findResponseById(Long id);

    Goal create(Goal goal);

    GoalResponse createGoal(CreateGoalRequest request);

    Goal update(Long id, Goal goal);

    GoalResponse updateGoal(Long id, UpdateGoalRequest request);

    void delete(Long id);

    void deleteGoal(Long id);
}
