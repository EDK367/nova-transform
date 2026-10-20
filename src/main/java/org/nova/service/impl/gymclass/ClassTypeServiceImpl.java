package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.ClassType;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.ClassRepository;
import org.nova.repository.ClassTypeRepository;
import org.nova.service.gymclass.ClassTypeService;
import org.nova.web.dto.request.gymclass.CreateClassTypeRequest;
import org.nova.web.dto.request.gymclass.UpdateClassTypeRequest;
import org.nova.web.dto.response.gymclass.ClassTypeResponse;
import org.nova.web.mapper.ClassTypeMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassTypeServiceImpl implements ClassTypeService {

    private final ClassTypeRepository repository;
    private final ClassRepository classRepository;
    private final ClassTypeMapper mapper;

    @Override
    public List<ClassType> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<ClassType> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassType create(ClassType classType) {
        if (repository.existsByName(classType.getName().trim())) {
            throw new ConflictException("Ya existe un tipo de clase con el nombre: " + classType.getName());
        }
        repository.persist(classType);
        return classType;
    }

    @Override
    @Transactional
    public ClassType update(Long id, ClassType classType) {
        ClassType existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(classType.getName().trim(), id)) {
            throw new ConflictException("Ya existe un tipo de clase con el nombre: " + classType.getName());
        }

        existing.setName(classType.getName().trim());
        existing.setDescription(classType.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClassType type = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + id));

        if (classRepository.countByClassTypeId(id) > 0) {
            throw new BadRequestException("No se puede eliminar el tipo de clase porque tiene clases asociadas");
        }

        repository.delete(type);
    }

    @Override
    public List<ClassTypeResponse> findAllResponses() {
        return mapper.toResponseList(repository.list("order by name asc"));
    }

    @Override
    public ClassTypeResponse findResponseById(Long id) {
        ClassType type = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + id));
        return mapper.toResponse(type);
    }

    @Override
    @Transactional
    public ClassTypeResponse create(CreateClassTypeRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un tipo de clase con el nombre: " + request.name());
        }
        ClassType entity = mapper.toEntity(request);
        entity.setName(request.name().trim());
        repository.persist(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ClassTypeResponse update(Long id, UpdateClassTypeRequest request) {
        ClassType existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe un tipo de clase con el nombre: " + request.name());
        }

        mapper.updateEntityFromRequest(existing, request);
        existing.setName(request.name().trim());
        return mapper.toResponse(existing);
    }
}