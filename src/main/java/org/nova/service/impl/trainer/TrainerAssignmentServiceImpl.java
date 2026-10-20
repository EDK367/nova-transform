package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.trainer.Trainer;
import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.exception.NotFoundException;
import org.nova.repository.MemberRepository;
import org.nova.repository.TrainerAssignmentRepository;
import org.nova.repository.TrainerRepository;
import org.nova.service.trainer.TrainerAssignmentService;
import org.nova.web.dto.request.trainer.CreateTrainerAssignmentRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerAssignmentRequest;
import org.nova.web.dto.response.trainer.TrainerAssignmentResponse;
import org.nova.web.mapper.TrainerAssignmentMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerAssignmentServiceImpl implements TrainerAssignmentService {

    private final TrainerAssignmentRepository repository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerAssignmentMapper mapper;

    @Override
    public List<TrainerAssignment> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<TrainerAssignmentResponse> findAllResponses(UUID memberId, UUID trainerId, Boolean activeOnly) {
        List<TrainerAssignment> list;
        if (memberId != null) {
            list = (activeOnly != null && activeOnly)
                    ? repository.findActiveByMemberId(memberId)
                    : repository.findByMemberId(memberId);
        } else if (trainerId != null) {
            list = repository.findByTrainerId(trainerId);
        } else {
            list = repository.listAllWithDetails();
        }
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<TrainerAssignment> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public TrainerAssignmentResponse findResponseById(Long id) {
        TrainerAssignment ta = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Asignacion de entrenador no encontrada con ID: " + id));
        return mapper.toResponse(ta);
    }

    @Override
    @Transactional
    public TrainerAssignment create(TrainerAssignment trainerAssignment) {
        repository.persist(trainerAssignment);
        return trainerAssignment;
    }

    @Override
    @Transactional
    public TrainerAssignmentResponse createAssignment(CreateTrainerAssignmentRequest request) {
        Member member = memberRepository.findByIdWithUser(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        Trainer trainer = trainerRepository.findByIdWithUser(request.trainerId())
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + request.trainerId()));

        TrainerAssignment assignment = new TrainerAssignment();
        assignment.setIdMember(member);
        assignment.setIdTrainer(trainer);
        assignment.setStartDate(request.startDate());
        assignment.setEndDate(request.endDate());
        assignment.setActive(true);
        assignment.setReason(request.reason() != null ? request.reason().trim() : null);

        repository.persist(assignment);
        return mapper.toResponse(assignment);
    }

    @Override
    @Transactional
    public TrainerAssignment update(Long id, TrainerAssignment trainerAssignment) {
        TrainerAssignment existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setIdMember(trainerAssignment.getIdMember());
        existing.setIdTrainer(trainerAssignment.getIdTrainer());
        existing.setStartDate(trainerAssignment.getStartDate());
        existing.setEndDate(trainerAssignment.getEndDate());
        existing.setActive(trainerAssignment.getActive());
        existing.setReason(trainerAssignment.getReason());
        return existing;
    }

    @Override
    @Transactional
    public TrainerAssignmentResponse updateAssignment(Long id, UpdateTrainerAssignmentRequest request) {
        TrainerAssignment assignment = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Asignacion de entrenador no encontrada con ID: " + id));

        if (request.startDate() != null) {
            assignment.setStartDate(request.startDate());
        }
        if (request.endDate() != null) {
            assignment.setEndDate(request.endDate());
        }
        if (request.active() != null) {
            assignment.setActive(request.active());
        }
        if (request.reason() != null) {
            assignment.setReason(request.reason().trim());
        }

        return mapper.toResponse(assignment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        TrainerAssignment assignment = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Asignacion de entrenador no encontrada con ID: " + id));
        repository.delete(assignment);
    }
}
