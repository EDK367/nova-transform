package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.gymclass.Waitlist;
import org.nova.domain.model.member.Member;
import org.nova.web.dto.response.gymclass.WaitlistResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WaitlistMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "classId", source = "idClass.id")
    @Mapping(target = "classTypeName", source = "idClass.idClassType.name")
    @Mapping(target = "classSchedule", source = "idClass.schedule")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", source = "idMember", qualifiedByName = "memberFullName")
    @Mapping(target = "memberEmail", source = "idMember.users.email")
    @Mapping(target = "registrationDate", source = "registrationDate")
    @Mapping(target = "notified", source = "notified")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    WaitlistResponse toResponse(Waitlist waitlist);

    List<WaitlistResponse> toResponseList(List<Waitlist> list);

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
