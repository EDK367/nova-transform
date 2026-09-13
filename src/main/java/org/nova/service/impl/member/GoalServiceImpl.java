package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Goal;
import org.nova.repository.GoalRepository;
import org.nova.service.member.GoalService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    @Override
    public List<Goal> findAll() {
        return goalRepository.listAll();
    }

    @Override
    public Optional<Goal> findById(Long id) {
        return goalRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Goal create(Goal goal) {
        goalRepository.persist(goal);
        return goal;
    }

    @Override
    @Transactional
    public Goal update(Long id, Goal goal) {
        Goal existing = goalRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(goal.getName());
        existing.setDescription(goal.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }
}