package org.nova.service.impl.staff;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BranchRepository;
import org.nova.repository.ReceptionistRepository;
import org.nova.repository.RoleRepository;
import org.nova.repository.UserRepository;
import org.nova.service.notification.CredentialNotificationService;
import org.nova.service.staff.ReceptionistService;
import org.nova.util.PasswordGenerator;
import org.nova.web.dto.request.staff.CreateReceptionistRequest;
import org.nova.web.dto.request.staff.UpdateReceptionistRequest;
import org.nova.web.dto.response.staff.ReceptionistResponse;
import org.nova.web.mapper.ReceptionistMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final PasswordGenerator passwordGenerator;
    private final CredentialNotificationService credentialNotificationService;
    private final ReceptionistMapper mapper;

    @Override
    public List<Receptionist> findAll() {
        return repository.listAllWithUser();
    }

    @Override
    public List<ReceptionistResponse> findAllResponses() {
        return mapper.toResponseList(repository.listAllWithUser());
    }

    @Override
    public Optional<Receptionist> findById(UUID id) {
        return repository.findByIdWithUser(id);
    }

    @Override
    public ReceptionistResponse findResponseById(UUID id) {
        Receptionist receptionist = repository.findByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Recepcionista no encontrado con ID: " + id));
        return mapper.toResponse(receptionist);
    }

    @Override
    @Transactional
    public Receptionist create(Receptionist receptionist) {
        repository.persist(receptionist);
        return receptionist;
    }

    @Override
    @Transactional
    public ReceptionistResponse createReceptionist(CreateReceptionistRequest request) {
        User user;

        if (request.userId() != null) {
            user = userRepository.findByIdOptional(request.userId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + request.userId()));

            if (repository.findByIdOptional(user.getId()).isPresent()) {
                throw new ConflictException("El usuario ya esta registrado como recepcionista");
            }
        } else {
            if (request.email() == null || request.email().isBlank()) {
                throw new BadRequestException("El correo electronico es obligatorio para crear un nuevo usuario");
            }
            if (request.username() == null || request.username().isBlank()) {
                throw new BadRequestException("El nombre de usuario es obligatorio para crear un nuevo usuario");
            }
            if (request.firstName() == null || request.firstName().isBlank()) {
                throw new BadRequestException("El nombre es obligatorio");
            }
            if (request.lastName() == null || request.lastName().isBlank()) {
                throw new BadRequestException("El apellido es obligatorio");
            }

            if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
                throw new ConflictException("El correo electronico ya esta en uso: " + request.email());
            }
            if (userRepository.existsByUsername(request.username().trim())) {
                throw new ConflictException("El nombre de usuario ya esta en uso: " + request.username());
            }

            Role role = roleRepository.findByName("RECEPCIONISTA")
                    .orElseThrow(() -> new NotFoundException("Rol RECEPCIONISTA no encontrado"));

            String tempPassword = passwordGenerator.generate();
            String hashedPassword = BcryptUtil.bcryptHash(tempPassword);

            user = new User();
            user.setId(UUID.randomUUID());
            user.setFirstName(request.firstName().trim());
            user.setLastName(request.lastName().trim());
            user.setEmail(request.email().trim().toLowerCase());
            user.setUsername(request.username().trim());
            user.setPhone(request.phone() != null ? request.phone().trim() : null);
            user.setPassword(hashedPassword);
            user.setActive(true);
            user.setTwoFactorAuth(false);
            user.setRole(role);

            if (request.branchId() != null) {
                Branch branch = branchRepository.findByIdOptional(request.branchId())
                        .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
                user.setBranch(branch);
            }

            userRepository.persist(user);
            credentialNotificationService.notifyCredentials(user.getEmail(), user.getUsername(), tempPassword);
        }

        Receptionist receptionist = new Receptionist();
        receptionist.setId(user.getId());
        receptionist.setUsers(user);
        receptionist.setHireDate(request.hireDate());

        repository.persist(receptionist);
        return mapper.toResponse(receptionist);
    }

    @Override
    @Transactional
    public Receptionist update(UUID id, Receptionist receptionist) {
        Receptionist existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setHireDate(receptionist.getHireDate());
        return existing;
    }

    @Override
    @Transactional
    public ReceptionistResponse updateReceptionist(UUID id, UpdateReceptionistRequest request) {
        Receptionist receptionist = repository.findByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Recepcionista no encontrado con ID: " + id));

        receptionist.setHireDate(request.hireDate());

        User user = receptionist.getUsers();
        if (user != null) {
            if (request.firstName() != null && !request.firstName().isBlank()) {
                user.setFirstName(request.firstName().trim());
            }
            if (request.lastName() != null && !request.lastName().isBlank()) {
                user.setLastName(request.lastName().trim());
            }
            if (request.phone() != null) {
                user.setPhone(request.phone().trim());
            }
            if (request.branchId() != null) {
                Branch branch = branchRepository.findByIdOptional(request.branchId())
                        .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
                user.setBranch(branch);
            }
            if (request.active() != null) {
                user.setActive(request.active());
            }
        }

        return mapper.toResponse(receptionist);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteReceptionist(UUID id) {
        Receptionist receptionist = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Recepcionista no encontrado con ID: " + id));
        repository.delete(receptionist);
    }
}
