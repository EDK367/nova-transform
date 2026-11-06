package org.nova.web.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nova.common.ApiResponse;
import org.nova.common.PageResponse;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.user.User;
import org.nova.exception.NotFoundException;
import org.nova.service.member.MemberService;
import org.nova.service.notification.EmailService;
import org.nova.service.report.PdfReportService;
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
    private final PdfReportService pdfReportService;
    private final EmailService emailService;

    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "MEMBERS_READ"})
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDirection,
            @QueryParam("order") String order
    ) {
        String effectiveDirection = (order != null && !order.isBlank()) ? order : sortDirection;
        PageResponse<MemberResponse> pagedMembers = memberService.findPaged(page, size, sortBy, effectiveDirection);
        return Response.ok(ApiResponse.success(200, "Clientes obtenidos exitosamente", pagedMembers)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "MEMBERS_READ"})
    public Response getById(@PathParam("id") UUID id) {
        return memberService.findById(id)
                .map(memberMapper::toResponse)
                .map(member -> Response.ok(ApiResponse.success(200, "Cliente obtenido exitosamente", member)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "MEMBERS_CREATE"})
    public Response create(@Valid CreateMemberRequest request) {
        CreateMemberResponse response = memberService.createMember(request);
        return Response.created(URI.create("/api/v1/members/" + response.id()))
                .entity(ApiResponse.success(201, "Cliente creado exitosamente", response))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "MEMBERS_DELETE"})
    public Response delete(@PathParam("id") UUID id) {
        memberService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/carnet-pdf")
    @Produces("application/pdf")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "ENTRENADOR", "CLIENTE"})
    public Response getCarnetPdf(@PathParam("id") UUID id) {
        byte[] pdfBytes = pdfReportService.generateMemberCarnetPdf(id);
        return Response.ok(pdfBytes)
                .header("Content-Disposition", "inline; filename=\"carnet_cliente_" + id + ".pdf\"")
                .header("Content-Length", pdfBytes.length)
                .build();
    }

    @POST
    @Path("/{id}/solicitar-carnet")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "RECEPCIONISTA", "CLIENTE"})
    public Response solicitarCarnet(@PathParam("id") UUID id) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));
        byte[] pdfBytes = pdfReportService.generateMemberCarnetPdf(id);
        User user = member.getUsers();
        String fullName = user.getFirstName() + " " + user.getLastName();
        emailService.sendMemberCarnet(user.getEmail(), user.getUsername(), fullName, pdfBytes);
        return Response.ok(ApiResponse.success(200, "Carnet enviado exitosamente al correo " + user.getEmail(), null)).build();
    }
}
