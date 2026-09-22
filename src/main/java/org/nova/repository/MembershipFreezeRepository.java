package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.membership.MembershipFreeze;

@ApplicationScoped
public class MembershipFreezeRepository implements PanacheRepositoryBase<MembershipFreeze, Long> {
}