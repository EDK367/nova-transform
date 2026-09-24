package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.Member;
import org.nova.web.dto.response.member.MemberResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "users.firstName")
    @Mapping(target = "lastName", source = "users.lastName")
    @Mapping(target = "email", source = "users.email")
    @Mapping(target = "phone", source = "users.phone")
    @Mapping(target = "username", source = "users.username")
    @Mapping(target = "roleName", source = "users.role.name")
    @Mapping(target = "branchId", source = "users.branch.id")
    @Mapping(target = "registrationDate", source = "registrationDate")
    @Mapping(target = "active", source = "users.active")
    MemberResponse toResponse(Member member);

    List<MemberResponse> toResponseList(List<Member> members);
}
