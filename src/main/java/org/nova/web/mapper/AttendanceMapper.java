package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.staff.Receptionist;
import org.nova.web.dto.response.gymclass.AttendanceResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "branchId", source = "idBranch.id")
    @Mapping(target = "branchName", source = "idBranch.name")
    @Mapping(target = "receptionistId", source = "idReceptionist.id")
    @Mapping(target = "receptionistName", source = "idReceptionist", qualifiedByName = "receptionistFullName")
    @Mapping(target = "memberId", source = "idMember.id")
    @Mapping(target = "memberName", source = "idMember", qualifiedByName = "memberFullName")
    @Mapping(target = "memberEmail", source = "idMember.users.email")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "entryTime", source = "entryTime")
    @Mapping(target = "exitTime", source = "exitTime")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    AttendanceResponse toResponse(Attendance attendance);

    List<AttendanceResponse> toResponseList(List<Attendance> attendances);

    @Named("memberFullName")
    default String memberFullName(Member member) {
        if (member == null || member.getUsers() == null) {
            return null;
        }
        String first = member.getUsers().getFirstName() != null ? member.getUsers().getFirstName() : "";
        String last = member.getUsers().getLastName() != null ? member.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }

    @Named("receptionistFullName")
    default String receptionistFullName(Receptionist receptionist) {
        if (receptionist == null || receptionist.getUsers() == null) {
            return null;
        }
        String first = receptionist.getUsers().getFirstName() != null ? receptionist.getUsers().getFirstName() : "";
        String last = receptionist.getUsers().getLastName() != null ? receptionist.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }
}
