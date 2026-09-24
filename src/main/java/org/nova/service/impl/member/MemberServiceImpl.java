package org.nova.service.impl.member;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BranchRepository;
import org.nova.repository.MemberRepository;
import org.nova.repository.RoleRepository;
import org.nova.repository.UserRepository;
import org.nova.service.member.MemberService;
import org.nova.service.notification.CredentialNotificationService;
import org.nova.util.PasswordGenerator;
import org.nova.common.PageResponse;
import org.nova.web.dto.request.member.CreateMemberRequest;
import org.nova.web.dto.response.member.CreateMemberResponse;
import org.nova.web.dto.response.member.MemberResponse;
import org.nova.web.mapper.MemberMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final PasswordGenerator passwordGenerator;
    private final CredentialNotificationService credentialNotificationService;
    private final MemberMapper memberMapper;

    @Override
    public List<Member> findAll() {
        return repository.findAllWithUser();
    }

    @Override
    public PageResponse<MemberResponse> findPaged(int page, int size) {
        return findPaged(page, size, "name", "asc");
    }

    @Override
    public PageResponse<MemberResponse> findPaged(int page, int size, String sortBy, String sortDirection) {
        PanacheQuery<Member> query = repository.findPagedWithUser(sortBy, sortDirection)
                .page(Page.of(page, size));
        List<MemberResponse> content = memberMapper.toResponseList(query.list());
        long totalElements = repository.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return repository.findByIdWithUser(id);
    }

    @Override
    @Transactional
    public Member create(Member member) {
        repository.persist(member);
        return member;
    }

    @Override
    @Transactional
    public CreateMemberResponse createMember(CreateMemberRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("El correo electrónico ya se encuentra registrado: " + request.email());
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("El nombre de usuario ya se encuentra registrado: " + request.username());
        }

        Role clienteRole = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new NotFoundException("Rol CLIENTE no encontrado en el sistema"));

        UUID userId = UUID.randomUUID();
        String tempPassword = passwordGenerator.generate();
        String hashedPassword = BcryptUtil.bcryptHash(tempPassword);

        User user = new User();
        user.setId(userId);
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhone(request.phone() != null ? request.phone().trim() : null);
        user.setUsername(request.username().trim());
        user.setPassword(hashedPassword);
        user.setActive(true);
        user.setTwoFactorAuth(false);
        user.setRole(clienteRole);

        if (request.branchId() != null) {
            Branch branch = branchRepository.findByIdOptional(request.branchId())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
            user.setBranch(branch);
        }

        userRepository.persist(user);

        Member member = new Member();
        member.setId(user.getId());
        member.setUsers(user);
        member.setRegistrationDate(LocalDate.now());

        repository.persist(member);

        credentialNotificationService.notifyCredentials(user.getEmail(), user.getUsername(), tempPassword);

        return new CreateMemberResponse(
                member.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                clienteRole.getName(),
                user.getBranch() != null ? user.getBranch().getId() : null,
                member.getRegistrationDate(),
                user.isActive(),
                tempPassword
        );
    }

    @Override
    @Transactional
    public Member update(UUID id, Member member) {
        Member existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setRegistrationDate(member.getRegistrationDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Member member = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + id));
        if (member.getUsers() != null) {
            member.getUsers().setActive(false);
        }
    }
}