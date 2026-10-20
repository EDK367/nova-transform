package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.Goal;
import org.nova.web.dto.response.member.GoalResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {

    GoalResponse toResponse(Goal goal);

    List<GoalResponse> toResponseList(List<Goal> goals);
}
