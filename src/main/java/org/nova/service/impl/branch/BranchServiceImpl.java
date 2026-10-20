package org.nova.service.impl.branch;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.branch.Branch;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BranchRepository;
import org.nova.service.branch.BranchService;
import org.nova.web.dto.request.branch.CreateBranchRequest;
import org.nova.web.dto.request.branch.UpdateBranchRequest;
import org.nova.web.dto.response.branch.BranchResponse;
import org.nova.web.mapper.BranchMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;

    @Override
    public List<Branch> findAll() {
        return repository.listAll();
    }

    @Override
    public List<BranchResponse> findAllResponses(Boolean activeOnly) {
        List<Branch> list = (activeOnly != null && activeOnly) ? repository.listActive() : repository.listAll();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public BranchResponse findResponseById(Long id) {
        Branch branch = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + id));
        return mapper.toResponse(branch);
    }

    @Override
    @Transactional
    public Branch create(Branch branch) {
        repository.persist(branch);
        return branch;
    }

    @Override
    @Transactional
    public BranchResponse createBranch(CreateBranchRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe una sucursal con el nombre: " + request.name().trim());
        }

        Branch branch = new Branch();
        branch.setName(request.name().trim());
        branch.setAddress(request.address().trim());
        branch.setPhone(request.phone() != null ? request.phone().trim() : null);
        branch.setActive(true);

        repository.persist(branch);
        return mapper.toResponse(branch);
    }

    @Override
    @Transactional
    public Branch update(Long id, Branch branch) {
        Branch existing = repository.findById(id);
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
    public BranchResponse updateBranch(Long id, UpdateBranchRequest request) {
        Branch branch = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otra sucursal con el nombre: " + request.name().trim());
        }

        branch.setName(request.name().trim());
        branch.setAddress(request.address().trim());
        branch.setPhone(request.phone() != null ? request.phone().trim() : null);
        if (request.active() != null) {
            branch.setActive(request.active());
        }

        return mapper.toResponse(branch);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBranch(Long id) {
        Branch branch = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + id));
        repository.delete(branch);
    }
}
