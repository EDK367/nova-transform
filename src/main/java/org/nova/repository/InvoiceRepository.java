package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.payment.Invoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepositoryBase<Invoice, Long> {

    public Optional<Invoice> findByIdWithDetails(Long id) {
        return find("select i from Invoice i " +
                "join fetch i.idBranch b " +
                "join fetch i.idMembership m " +
                "join fetch m.idMember mem " +
                "join fetch mem.users memUser " +
                "join fetch m.idPlan p " +
                "join fetch i.idReceptionist rec " +
                "join fetch rec.users recUser " +
                "left join fetch i.idPromotion promo " +
                "where i.id = ?1", id).firstResultOptional();
    }

    public List<Invoice> findByMembershipId(Long membershipId) {
        return find("select i from Invoice i " +
                "join fetch i.idBranch b " +
                "join fetch i.idMembership m " +
                "join fetch m.idMember mem " +
                "join fetch mem.users memUser " +
                "join fetch m.idPlan p " +
                "join fetch i.idReceptionist rec " +
                "join fetch rec.users recUser " +
                "left join fetch i.idPromotion promo " +
                "where m.id = ?1 order by i.createdAt desc", membershipId).list();
    }

    public List<Invoice> findByMemberId(UUID memberId) {
        return find("select i from Invoice i " +
                "join fetch i.idBranch b " +
                "join fetch i.idMembership m " +
                "join fetch m.idMember mem " +
                "join fetch mem.users memUser " +
                "join fetch m.idPlan p " +
                "join fetch i.idReceptionist rec " +
                "join fetch rec.users recUser " +
                "left join fetch i.idPromotion promo " +
                "where mem.id = ?1 order by i.createdAt desc", memberId).list();
    }

    public PanacheQuery<Invoice> findFiltered(InvoiceStatus status, Long branchId, UUID memberId, Sort sort) {
        StringBuilder query = new StringBuilder("select i from Invoice i " +
                "join fetch i.idBranch b " +
                "join fetch i.idMembership m " +
                "join fetch m.idMember mem " +
                "join fetch mem.users memUser " +
                "join fetch m.idPlan p " +
                "join fetch i.idReceptionist rec " +
                "join fetch rec.users recUser " +
                "left join fetch i.idPromotion promo where 1=1");
        Parameters params = new Parameters();

        if (status != null) {
            query.append(" and i.status = :status");
            params.and("status", status);
        }
        if (branchId != null) {
            query.append(" and b.id = :branchId");
            params.and("branchId", branchId);
        }
        if (memberId != null) {
            query.append(" and mem.id = :memberId");
            params.and("memberId", memberId);
        }

        return find(query.toString(), sort, params);
    }
}