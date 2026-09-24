package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.common.PageResponse;
import org.nova.service.member.MemberService;
import org.nova.web.dto.request.member.CreateMemberRequest;
import org.nova.web.dto.response.member.CreateMemberResponse;
import org.nova.web.dto.response.member.MemberResponse;
import org.nova.web.mapper.MemberMapper;

import java.net.URI;
import java.util.UUID;

@Path("/api/v1/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MemberResource {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @GET
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDirection,
            @QueryParam("order") String order
    ) {
        String effectiveDirection = (order != null && !order.isBlank()) ? order : sortDirection;
        PageResponse<MemberResponse> pagedMembers = memberService.findPaged(page, size, sortBy, effectiveDirection);
        return Response.ok(ApiResponse.success(200, "Socios obtenidos exitosamente", pagedMembers)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response getById(@PathParam("id") UUID id) {
        return memberService.findById(id)
                .map(memberMapper::toResponse)
                .map(member -> Response.ok(ApiResponse.success(200, "Socio obtenido exitosamente", member)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed({"ADMIN", "RECEPCIONISTA"})
    public Response create(@Valid CreateMemberRequest request) {
        CreateMemberResponse response = memberService.createMember(request);
        return Response.created(URI.create("/api/v1/members/" + response.id()))
                .entity(ApiResponse.success(201, "Socio creado exitosamente", response))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") UUID id) {
        memberService.delete(id);
        return Response.noContent().build();
    }
}
