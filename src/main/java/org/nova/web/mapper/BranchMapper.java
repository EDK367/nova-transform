package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.branch.Branch;
import org.nova.web.dto.response.branch.BranchResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {

    BranchResponse toResponse(Branch branch);

    List<BranchResponse> toResponseList(List<Branch> branches);
}
