package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Goal;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.GoalRepository;
import org.nova.service.member.GoalService;
import org.nova.web.dto.request.member.CreateGoalRequest;
import org.nova.web.dto.request.member.UpdateGoalRequest;
import org.nova.web.dto.response.member.GoalResponse;
import org.nova.web.mapper.GoalMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository repository;
    private final GoalMapper mapper;

    @Override
    public List<Goal> findAll() {
        return repository.listAll();
    }

    @Override
    public List<GoalResponse> findAllResponses() {
        return mapper.toResponseList(repository.listAll());
    }

    @Override
    public Optional<Goal> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public GoalResponse findResponseById(Long id) {
        Goal goal = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Objetivo no encontrado con ID: " + id));
        return mapper.toResponse(goal);
    }

    @Override
    @Transactional
    public Goal create(Goal goal) {
        repository.persist(goal);
        return goal;
    }

    @Override
    @Transactional
    public GoalResponse createGoal(CreateGoalRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un objetivo con el nombre: " + request.name().trim());
        }

        Goal goal = new Goal();
        goal.setName(request.name().trim());
        goal.setDescription(request.description() != null ? request.description().trim() : null);

        repository.persist(goal);
        return mapper.toResponse(goal);
    }

    @Override
    @Transactional
    public Goal update(Long id, Goal goal) {
        Goal existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(goal.getName());
        existing.setDescription(goal.getDescription());
        return existing;
    }

    @Override
    @Transactional
    public GoalResponse updateGoal(Long id, UpdateGoalRequest request) {
        Goal goal = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Objetivo no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otro objetivo con el nombre: " + request.name().trim());
        }

        goal.setName(request.name().trim());
        goal.setDescription(request.description() != null ? request.description().trim() : null);

        return mapper.toResponse(goal);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id) {
        Goal goal = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Objetivo no encontrado con ID: " + id));
        repository.delete(goal);
    }
}
