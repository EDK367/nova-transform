package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.Benefit;

@ApplicationScoped
public class BenefitRepository implements PanacheRepositoryBase<Benefit, Long> {
}