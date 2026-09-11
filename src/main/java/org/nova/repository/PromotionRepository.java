package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.Promotion;

@ApplicationScoped
public class PromotionRepository implements PanacheRepositoryBase<Promotion, Long> {
}