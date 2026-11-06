package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.user.Permission;
import org.nova.domain.model.user.Role;
import org.nova.web.dto.response.permission.PermissionResponse;
import org.nova.web.dto.response.role.RoleResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "permissions", expression = "java(mapPermissions(role.getPermissions()))")
    RoleResponse toResponse(Role role);

    List<RoleResponse> toResponseList(List<Role> roles);

    default List<PermissionResponse> mapPermissions(Set<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return List.of();
        }
        return permissions.stream()
                .sorted(Comparator.comparing(Permission::getModule, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Permission::getCode, Comparator.nullsLast(String::compareTo)))
                .map(p -> new PermissionResponse(p.getId(), p.getCode(), p.getName(), p.getModule(), p.getDescription()))
                .toList();
    }
}
