package org.nova.service.impl.membership;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.model.membership.Benefit;
import org.nova.domain.model.membership.BenefitPlan;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.domain.model.membership.PlanPriceHistory;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BenefitPlanRepository;
import org.nova.repository.BenefitRepository;
import org.nova.repository.MembershipPlanRepository;
import org.nova.repository.PlanPriceHistoryRepository;
import org.nova.service.membership.MembershipPlanService;
import org.nova.web.dto.request.membership.CreateMembershipPlanRequest;
import org.nova.web.dto.request.membership.UpdateMembershipPlanRequest;
import org.nova.web.dto.response.membership.BenefitResponse;
import org.nova.web.dto.response.membership.MembershipPlanResponse;
import org.nova.web.dto.response.membership.PlanPriceHistoryResponse;
import org.nova.web.mapper.BenefitMapper;
import org.nova.web.mapper.MembershipPlanMapper;
import org.nova.web.mapper.PlanPriceHistoryMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository repository;
    private final BenefitRepository benefitRepository;
    private final BenefitPlanRepository benefitPlanRepository;
    private final PlanPriceHistoryRepository priceHistoryRepository;
    private final MembershipPlanMapper mapper;
    private final BenefitMapper benefitMapper;
    private final PlanPriceHistoryMapper priceHistoryMapper;

    @Override
    public List<MembershipPlan> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<MembershipPlan> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public MembershipPlan create(MembershipPlan membershipPlan) {
        repository.persist(membershipPlan);
        return membershipPlan;
    }

    @Override
    @Transactional
    public MembershipPlan update(Long id, MembershipPlan membershipPlan) {
        MembershipPlan existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(membershipPlan.getName());
        existing.setDuration(membershipPlan.getDuration());
        existing.setPrice(membershipPlan.getPrice());
        existing.setDescription(membershipPlan.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<MembershipPlanResponse> findAllResponses() {
        List<MembershipPlan> plans = repository.listAll(Sort.by("price", Sort.Direction.Ascending));
        List<MembershipPlanResponse> responses = new ArrayList<>();
        for (MembershipPlan plan : plans) {
            List<BenefitResponse> benefits = getPlanBenefits(plan.getId());
            responses.add(mapper.toResponseWithBenefits(plan, benefits));
        }
        return responses;
    }

    @Override
    public PageResponse<MembershipPlanResponse> findPaged(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc"))
                ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "price";
        Sort sort = Sort.by(property, direction);

        PanacheQuery<MembershipPlan> query = repository.findAll(sort)
                .page(Page.of(page, size));

        List<MembershipPlanResponse> content = new ArrayList<>();
        for (MembershipPlan plan : query.list()) {
            List<BenefitResponse> benefits = getPlanBenefits(plan.getId());
            content.add(mapper.toResponseWithBenefits(plan, benefits));
        }

        long totalElements = repository.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public MembershipPlanResponse findResponseById(Long id) {
        MembershipPlan plan = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Plan de membresía no encontrado con ID: " + id));
        List<BenefitResponse> benefits = getPlanBenefits(id);
        return mapper.toResponseWithBenefits(plan, benefits);
    }

    @Override
    @Transactional
    public MembershipPlanResponse createPlan(CreateMembershipPlanRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un plan con el nombre: " + request.name().trim());
        }

        MembershipPlan plan = new MembershipPlan();
        plan.setName(request.name().trim());
        plan.setDuration(request.duration());
        plan.setPrice(request.price());
        plan.setDescription(request.description() != null ? request.description().trim() : null);

        repository.persist(plan);

        // Asociar beneficios si se enviaron
        List<BenefitResponse> benefits = new ArrayList<>();
        if (request.benefitIds() != null && !request.benefitIds().isEmpty()) {
            for (Long benefitId : request.benefitIds()) {
                Benefit benefit = benefitRepository.findByIdOptional(benefitId)
                        .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + benefitId));

                BenefitPlan benefitPlan = new BenefitPlan();
                benefitPlan.setIdPlan(plan);
                benefitPlan.setIdBenefit(benefit);
                benefitPlanRepository.persist(benefitPlan);
                benefits.add(benefitMapper.toResponse(benefit));
            }
        }

        // Registrar precio inicial en historial
        PlanPriceHistory priceHistory = new PlanPriceHistory();
        priceHistory.setIdPlan(plan);
        priceHistory.setPrice(plan.getPrice());
        priceHistory.setStartDate(LocalDate.now());
        priceHistory.setObservation("Tarifa inicial de lanzamiento");
        priceHistoryRepository.persist(priceHistory);

        return mapper.toResponseWithBenefits(plan, benefits);
    }

    @Override
    @Transactional
    public MembershipPlanResponse updatePlan(Long id, UpdateMembershipPlanRequest request) {
        MembershipPlan plan = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Plan de membresía no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otro plan con el nombre: " + request.name().trim());
        }

        // Si el precio cambió, actualizar el historial de precios
        if (plan.getPrice().compareTo(request.price()) != 0) {
            Optional<PlanPriceHistory> latestHistory = priceHistoryRepository.findLatestByPlanId(id);
            if (latestHistory.isPresent()) {
                PlanPriceHistory previous = latestHistory.get();
                if (previous.getEndDate() == null) {
                    previous.setEndDate(LocalDate.now());
                }
            }

            PlanPriceHistory newHistory = new PlanPriceHistory();
            newHistory.setIdPlan(plan);
            newHistory.setPrice(request.price());
            newHistory.setStartDate(LocalDate.now());
            newHistory.setObservation("Actualización de tarifa");
            priceHistoryRepository.persist(newHistory);
        }

        plan.setName(request.name().trim());
        plan.setDuration(request.duration());
        plan.setPrice(request.price());
        plan.setDescription(request.description() != null ? request.description().trim() : null);

        List<BenefitResponse> benefits = getPlanBenefits(id);
        return mapper.toResponseWithBenefits(plan, benefits);
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        MembershipPlan plan = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Plan de membresía no encontrado con ID: " + id));

        benefitPlanRepository.deleteByPlanId(id);
        repository.delete(plan);
    }

    @Override
    @Transactional
    public void addBenefitToPlan(Long planId, Long benefitId) {
        MembershipPlan plan = repository.findByIdOptional(planId)
                .orElseThrow(() -> new NotFoundException("Plan no encontrado con ID: " + planId));
        Benefit benefit = benefitRepository.findByIdOptional(benefitId)
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + benefitId));

        if (benefitPlanRepository.findByPlanIdAndBenefitId(planId, benefitId).isPresent()) {
            throw new ConflictException("El beneficio ya se encuentra asignado a este plan");
        }

        BenefitPlan benefitPlan = new BenefitPlan();
        benefitPlan.setIdPlan(plan);
        benefitPlan.setIdBenefit(benefit);
        benefitPlanRepository.persist(benefitPlan);
    }

    @Override
    @Transactional
    public void removeBenefitFromPlan(Long planId, Long benefitId) {
        repository.findByIdOptional(planId)
                .orElseThrow(() -> new NotFoundException("Plan no encontrado con ID: " + planId));
        benefitRepository.findByIdOptional(benefitId)
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + benefitId));

        long deleted = benefitPlanRepository.deleteByPlanIdAndBenefitId(planId, benefitId);
        if (deleted == 0) {
            throw new NotFoundException("La asignación del beneficio al plan no existe");
        }
    }

    @Override
    public List<BenefitResponse> getPlanBenefits(Long planId) {
        List<BenefitPlan> benefitPlans = benefitPlanRepository.findByPlanId(planId);
        List<BenefitResponse> responses = new ArrayList<>();
        for (BenefitPlan bp : benefitPlans) {
            responses.add(benefitMapper.toResponse(bp.getIdBenefit()));
        }
        return responses;
    }

    @Override
    public List<PlanPriceHistoryResponse> getPriceHistories(Long planId) {
        repository.findByIdOptional(planId)
                .orElseThrow(() -> new NotFoundException("Plan no encontrado con ID: " + planId));
        return priceHistoryMapper.toResponseList(priceHistoryRepository.findByPlanId(planId));
    }
}