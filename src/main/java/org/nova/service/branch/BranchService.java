package org.nova.service.branch;

import org.nova.domain.model.branch.Branch;
import org.nova.web.dto.request.branch.CreateBranchRequest;
import org.nova.web.dto.request.branch.UpdateBranchRequest;
import org.nova.web.dto.response.branch.BranchResponse;

import java.util.List;
import java.util.Optional;

public interface BranchService {

    List<Branch> findAll();

    List<BranchResponse> findAllResponses(Boolean activeOnly);

    Optional<Branch> findById(Long id);

    BranchResponse findResponseById(Long id);

    Branch create(Branch branch);

    BranchResponse createBranch(CreateBranchRequest request);

    Branch update(Long id, Branch branch);

    BranchResponse updateBranch(Long id, UpdateBranchRequest request);

    void delete(Long id);

    void deleteBranch(Long id);
}
