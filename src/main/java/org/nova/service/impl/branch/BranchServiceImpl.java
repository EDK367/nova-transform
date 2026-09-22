package org.nova.service.impl.branch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.branch.Branch;
import org.nova.repository.BranchRepository;
import org.nova.service.branch.BranchService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    public List<Branch> findAll() {
        return branchRepository.listAll();
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return branchRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Branch create(Branch branch) {
        branchRepository.persist(branch);
        return branch;
    }

    @Override
    @Transactional
    public Branch update(Long id, Branch branch) {
        Branch existing = branchRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(branch.getName());
        existing.setAddress(branch.getAddress());
        existing.setPhone(branch.getPhone());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        branchRepository.deleteById(id);
    }
}
