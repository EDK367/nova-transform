package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.web.dto.response.membership.MembershipResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MembershipMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", source = "idMember", qualifiedByName = "memberFullName")
    @Mapping(target = "memberEmail", source = "idMember.users.email")
    @Mapping(target = "planId", source = "idPlan.id")
    @Mapping(target = "planName", source = "idPlan.name")
    @Mapping(target = "planDuration", source = "idPlan.duration")
    @Mapping(target = "planPrice", source = "idPlan.price")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "createdAt", source = "createdAt")
    MembershipResponse toResponse(Membership membership);

    List<MembershipResponse> toResponseList(List<Membership> memberships);

    @Named("memberFullName")
    default String memberFullName(Member member) {
        if (member == null || member.getUsers() == null) {
            return null;
        }
        String first = member.getUsers().getFirstName() != null ? member.getUsers().getFirstName() : "";
        String last = member.getUsers().getLastName() != null ? member.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }
}
