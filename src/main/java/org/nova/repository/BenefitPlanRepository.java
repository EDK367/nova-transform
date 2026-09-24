package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.BenefitPlan;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BenefitPlanRepository implements PanacheRepositoryBase<BenefitPlan, Long> {

    public List<BenefitPlan> findByPlanId(Long planId) {
        return find("idPlan.id", planId).list();
    }

    public Optional<BenefitPlan> findByPlanIdAndBenefitId(Long planId, Long benefitId) {
        return find("idPlan.id = ?1 and idBenefit.id = ?2", planId, benefitId).firstResultOptional();
    }

    public long deleteByPlanIdAndBenefitId(Long planId, Long benefitId) {
        return delete("idPlan.id = ?1 and idBenefit.id = ?2", planId, benefitId);
    }

    public long deleteByPlanId(Long planId) {
        return delete("idPlan.id", planId);
    }
}