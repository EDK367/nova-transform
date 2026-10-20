package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.user.Role;
import org.nova.web.dto.response.role.RoleResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleResponse toResponse(Role role);

    List<RoleResponse> toResponseList(List<Role> roles);
}
