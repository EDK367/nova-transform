package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.MemberGoal;
import org.nova.web.dto.response.member.MemberGoalResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberGoalMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", expression = "java(mg.getIdMember() != null && mg.getIdMember().getUsers() != null ? mg.getIdMember().getUsers().getFirstName() + \" \" + mg.getIdMember().getUsers().getLastName() : null)")
    @Mapping(target = "goalId", source = "idGoal.id")
    @Mapping(target = "goalName", source = "idGoal.name")
    @Mapping(target = "goalDescription", source = "idGoal.description")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    MemberGoalResponse toResponse(MemberGoal mg);

    List<MemberGoalResponse> toResponseList(List<MemberGoal> list);
}
