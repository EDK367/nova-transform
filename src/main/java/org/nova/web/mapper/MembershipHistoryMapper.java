package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.membership.MembershipHistory;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MembershipHistoryMapper {

    @Mapping(target = "membershipId", source = "idMembership.id")
    MembershipHistoryResponse toResponse(MembershipHistory history);

    List<MembershipHistoryResponse> toResponseList(List<MembershipHistory> histories);
}
