package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.user.User;
import org.nova.web.dto.response.user.UserResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "branchId", source = "branch.id")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
