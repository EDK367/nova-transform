package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.user.Permission;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.web.dto.response.user.UserResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "permissions", expression = "java(extractPermissionCodes(user.getRole()))")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    default List<String> extractPermissionCodes(Role role) {
        if (role == null || role.getPermissions() == null) {
            return List.of();
        }
        return role.getPermissions().stream()
                .map(Permission::getCode)
                .sorted()
                .toList();
    }
}
