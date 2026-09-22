package org.nova.service.branch;

import org.nova.domain.model.branch.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchService {
    List<Branch> findAll();

    Optional<Branch> findById(Long id);

    Branch create(Branch branch);

    Branch update(Long id, Branch branch);

    void delete (Long id);
}
