package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.MembershipFreeze;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MembershipFreezeMapper {

    @Mapping(target = "membershipId", source = "idMembership.id")
    MembershipFreezeResponse toResponse(MembershipFreeze freeze);

    List<MembershipFreezeResponse> toResponseList(List<MembershipFreeze> freezes);
}
