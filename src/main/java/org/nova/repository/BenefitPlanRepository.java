package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.BenefitPlan;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BenefitPlanRepository implements PanacheRepositoryBase<BenefitPlan, Long> {

    public List<BenefitPlan> findByPlanId(Long planId) {
        return find("select bp from BenefitPlan bp left join fetch bp.idPlan left join fetch bp.idBenefit where bp.idPlan.id = ?1", planId).list();
    }

    public Optional<BenefitPlan> findByPlanIdAndBenefitId(Long planId, Long benefitId) {
        return find("idPlan.id = ?1 and idBenefit.id = ?2", planId, benefitId).firstResultOptional();
    }

    public boolean existsByPlanAndBenefit(Long planId, Long benefitId) {
        return count("idPlan.id = ?1 and idBenefit.id = ?2", planId, benefitId) > 0;
    }

    public List<BenefitPlan> listAllWithDetails() {
        return find("select bp from BenefitPlan bp left join fetch bp.idPlan left join fetch bp.idBenefit").list();
    }

    public Optional<BenefitPlan> findByIdWithDetails(Long id) {
        return find("select bp from BenefitPlan bp left join fetch bp.idPlan left join fetch bp.idBenefit where bp.id = ?1", id).firstResultOptional();
    }

    public long deleteByPlanIdAndBenefitId(Long planId, Long benefitId) {
        return delete("idPlan.id = ?1 and idBenefit.id = ?2", planId, benefitId);
    }

    public long deleteByPlanId(Long planId) {
        return delete("idPlan.id", planId);
    }
}
