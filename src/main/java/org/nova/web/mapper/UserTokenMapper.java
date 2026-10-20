package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.user.UserToken;
import org.nova.web.dto.response.user.UserTokenResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTokenMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "idUser.id")
    @Mapping(target = "username", source = "idUser.username")
    @Mapping(target = "userEmail", source = "idUser.email")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "generationDate", source = "generationDate")
    @Mapping(target = "expirationDate", source = "expirationDate")
    @Mapping(target = "used", source = "used")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserTokenResponse toResponse(UserToken ut);

    List<UserTokenResponse> toResponseList(List<UserToken> list);
}
