package org.nova.service.member;

import org.nova.domain.model.member.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalService {
    List<Goal> findAll();

    Optional<Goal> findById(Long id);

    Goal create(Goal goal);

    Goal update(Long id, Goal goal);

    void delete(Long id);
}
