package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.ClassType;
import org.nova.repository.ClassTypeRepository;
import org.nova.service.gymclass.ClassTypeService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassTypeServiceImpl implements ClassTypeService {

    private final ClassTypeRepository classTypeRepository;

    @Override
    public List<ClassType> findAll() {
        return classTypeRepository.listAll();
    }

    @Override
    public Optional<ClassType> findById(Long id) {
        return classTypeRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassType create(ClassType classType) {
        classTypeRepository.persist(classType);
        return classType;
    }

    @Override
    @Transactional
    public ClassType update(Long id, ClassType classType) {
        ClassType existing = classTypeRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(classType.getName());
        existing.setDescription(classType.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        classTypeRepository.deleteById(id);
    }
}