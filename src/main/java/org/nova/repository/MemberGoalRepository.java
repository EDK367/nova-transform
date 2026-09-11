package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.MemberGoal;

@ApplicationScoped
public class MemberGoalRepository implements PanacheRepositoryBase<MemberGoal, Long> {
}