package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.gymclass.ClassRating;
import org.nova.domain.model.member.Member;
import org.nova.web.dto.response.gymclass.ClassRatingResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassRatingMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "enrollmentId", source = "idEnrollment.id")
    @Mapping(target = "classId", source = "idEnrollment.idClass.id")
    @Mapping(target = "classTypeName", source = "idEnrollment.idClass.idClassType.name")
    @Mapping(target = "memberId", source = "idEnrollment.idMember.id")
    @Mapping(target = "memberName", source = "idEnrollment.idMember", qualifiedByName = "memberFullName")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "dateTime", source = "dateTime")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ClassRatingResponse toResponse(ClassRating rating);

    List<ClassRatingResponse> toResponseList(List<ClassRating> ratings);

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
