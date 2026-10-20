package org.nova.service.impl.user;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.trainer.Trainer;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BranchRepository;
import org.nova.repository.ReceptionistRepository;
import org.nova.repository.RoleRepository;
import org.nova.repository.TrainerRepository;
import org.nova.repository.UserRepository;
import org.nova.service.notification.CredentialNotificationService;
import org.nova.service.user.UserService;
import org.nova.util.PasswordGenerator;
import org.nova.web.dto.request.user.CreateUserRequest;
import org.nova.web.dto.request.user.UpdateUserRequest;
import org.nova.web.dto.response.user.CreateUserResponse;
import org.nova.web.dto.response.user.UserResponse;
import org.nova.web.mapper.UserMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final TrainerRepository trainerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordGenerator passwordGenerator;
    private final CredentialNotificationService credentialNotificationService;
    private final UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return repository.listAll();
    }

    @Override
    public PageResponse<UserResponse> findPaged(int page, int size) {
        return findPaged(page, size, "name", "asc");
    }

    @Override
    public PageResponse<UserResponse> findPaged(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc"))
                ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        Sort sort;
        if (sortBy != null && sortBy.equalsIgnoreCase("email")) {
            sort = Sort.by("email", direction);
        } else {
            sort = Sort.by("firstName", direction).and("lastName", direction);
        }
        PanacheQuery<User> query = repository.findAll(sort)
                .page(Page.of(page, size));
        List<UserResponse> content = userMapper.toResponseList(query.list());
        long totalElements = repository.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public User create(User user) {
        repository.persist(user);
        return user;
    }

    @Override
    @Transactional
    public CreateUserResponse createInternalUser(CreateUserRequest request) {
        if ((request.role() == null || request.role().isBlank()) && request.roleId() == null) {
            throw new BadRequestException("El rol es obligatorio");
        }

        Role role;
        if (request.role() != null && !request.role().isBlank()) {
            role = roleRepository.findByName(request.role().trim().toUpperCase())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + request.role()));
        } else {
            role = roleRepository.findByIdOptional(request.roleId())
                    .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + request.roleId()));
        }

        String roleName = role.getName().trim().toUpperCase();
        if ("ADMIN".equals(roleName)) {
            throw new BadRequestException("No está permitido crear usuarios con el rol ADMIN desde este módulo");
        }
        if ("CLIENTE".equals(roleName)) {
            throw new BadRequestException("No está permitido crear usuarios con el rol CLIENTE desde este módulo. Los clientes deben crearse a través del flujo de socios");
        }
        if (!"ENTRENADOR".equals(roleName) && !"RECEPCIONISTA".equals(roleName)) {
            throw new BadRequestException("Rol no permitido para usuarios internos: " + roleName);
        }

        if (repository.existsByEmail(request.email())) {
            throw new ConflictException("El correo electrónico ya se encuentra registrado: " + request.email());
        }

        if (repository.existsByUsername(request.username())) {
            throw new ConflictException("El nombre de usuario ya se encuentra registrado: " + request.username());
        }

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
        user.setRole(role);

        if (request.branchId() != null) {
            Branch branch = branchRepository.findByIdOptional(request.branchId())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
            user.setBranch(branch);
        }

        repository.persist(user);

        LocalDate hireDate = request.hireDate() != null ? request.hireDate() : LocalDate.now();

        if ("ENTRENADOR".equals(roleName)) {
            Trainer trainer = new Trainer();
            trainer.setId(user.getId());
            trainer.setUsers(user);
            trainer.setHireDate(hireDate);
            trainerRepository.persist(trainer);
        } else if ("RECEPCIONISTA".equals(roleName)) {
            Receptionist receptionist = new Receptionist();
            receptionist.setId(user.getId());
            receptionist.setUsers(user);
            receptionist.setHireDate(hireDate);
            receptionistRepository.persist(receptionist);
        }

        credentialNotificationService.notifyCredentials(user.getEmail(), user.getUsername(), tempPassword);

        return new CreateUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                role.getName(),
                user.getBranch() != null ? user.getBranch().getId() : null,
                hireDate,
                user.isActive(),
                tempPassword
        );
    }

    @Override
    @Transactional
    public User update(UUID id, User user) {
        User existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setUsername(user.getUsername());
        existing.setTwoFactorAuth(user.isTwoFactorAuth());
        existing.setRole(user.getRole());
        existing.setBranch(user.getBranch());

        return existing;
    }

    @Override
    @Transactional
    public User update(UUID id, UpdateUserRequest request) {
        User existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));

        if (!existing.getEmail().equalsIgnoreCase(request.email()) && repository.existsByEmail(request.email())) {
            throw new ConflictException("El correo electrónico ya se encuentra registrado: " + request.email());
        }

        if (!existing.getUsername().equals(request.username()) && repository.existsByUsername(request.username())) {
            throw new ConflictException("El nombre de usuario ya se encuentra registrado: " + request.username());
        }

        existing.setFirstName(request.firstName().trim());
        existing.setLastName(request.lastName().trim());
        existing.setEmail(request.email().trim().toLowerCase());
        existing.setPhone(request.phone() != null ? request.phone().trim() : null);
        existing.setUsername(request.username().trim());

        if (request.branchId() != null) {
            Branch branch = branchRepository.findByIdOptional(request.branchId())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
            existing.setBranch(branch);
        } else {
            existing.setBranch(null);
        }

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
        existing.setActive(false);
    }
}