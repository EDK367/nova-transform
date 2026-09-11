package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.branch.Branch;

@ApplicationScoped
public class BranchRepository implements PanacheRepositoryBase<Branch, Long> {
}
