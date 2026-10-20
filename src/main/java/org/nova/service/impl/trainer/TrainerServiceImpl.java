package org.nova.service.impl.trainer;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.trainer.Trainer;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BranchRepository;
import org.nova.repository.RoleRepository;
import org.nova.repository.TrainerRepository;
import org.nova.repository.UserRepository;
import org.nova.service.notification.CredentialNotificationService;
import org.nova.service.trainer.TrainerService;
import org.nova.util.PasswordGenerator;
import org.nova.web.dto.request.trainer.CreateTrainerRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerRequest;
import org.nova.web.dto.response.trainer.TrainerResponse;
import org.nova.web.mapper.TrainerMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final PasswordGenerator passwordGenerator;
    private final CredentialNotificationService credentialNotificationService;
    private final TrainerMapper mapper;

    @Override
    public List<Trainer> findAll() {
        return repository.listAllWithUser();
    }

    @Override
    public List<TrainerResponse> findAllResponses() {
        return mapper.toResponseList(repository.listAllWithUser());
    }

    @Override
    public Optional<Trainer> findById(UUID id) {
        return repository.findByIdWithUser(id);
    }

    @Override
    public TrainerResponse findResponseById(UUID id) {
        Trainer trainer = repository.findByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + id));
        return mapper.toResponse(trainer);
    }

    @Override
    @Transactional
    public Trainer create(Trainer trainer) {
        repository.persist(trainer);
        return trainer;
    }

    @Override
    @Transactional
    public TrainerResponse createTrainer(CreateTrainerRequest request) {
        User user;

        if (request.userId() != null) {
            user = userRepository.findByIdOptional(request.userId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + request.userId()));

            if (repository.findByIdOptional(user.getId()).isPresent()) {
                throw new ConflictException("El usuario ya esta registrado como entrenador");
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

            Role role = roleRepository.findByName("ENTRENADOR")
                    .orElseThrow(() -> new NotFoundException("Rol ENTRENADOR no encontrado"));

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

        Trainer trainer = new Trainer();
        trainer.setId(user.getId());
        trainer.setUsers(user);
        trainer.setHireDate(request.hireDate());

        repository.persist(trainer);
        return mapper.toResponse(trainer);
    }

    @Override
    @Transactional
    public Trainer update(UUID id, Trainer trainer) {
        Trainer existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setHireDate(trainer.getHireDate());
        return existing;
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(UUID id, UpdateTrainerRequest request) {
        Trainer trainer = repository.findByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + id));

        trainer.setHireDate(request.hireDate());

        User user = trainer.getUsers();
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

        return mapper.toResponse(trainer);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteTrainer(UUID id) {
        Trainer trainer = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + id));
        repository.delete(trainer);
    }
}
