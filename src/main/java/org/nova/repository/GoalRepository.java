package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.Goal;

@ApplicationScoped
public class GoalRepository implements PanacheRepositoryBase<Goal, Long> {
}