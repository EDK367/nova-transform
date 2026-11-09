package org.nova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.domain.model.workout.Routine;
import org.nova.exception.NotFoundException;
import org.nova.repository.RoutineRepository;
import org.nova.repository.TrainerAssignmentRepository;
import org.nova.service.impl.workout.RoutineServiceImpl;
import org.nova.web.dto.request.workout.CreateRoutineRequest;
import org.nova.web.dto.request.workout.UpdateRoutineRequest;
import org.nova.web.dto.response.workout.RoutineResponse;
import org.nova.web.mapper.RoutineMapper;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutineServiceUnitTest {

    @Mock
    private RoutineRepository repository;

    @Mock
    private TrainerAssignmentRepository assignmentRepository;

    @Mock
    private RoutineMapper mapper;

    @InjectMocks
    private RoutineServiceImpl routineService;

    private TrainerAssignment sampleAssignment;
    private Routine sampleRoutine;

    @BeforeEach
    void setUp() {
        sampleAssignment = new TrainerAssignment();
        sampleAssignment.setId(10L);
        sampleAssignment.setActive(true);

        sampleRoutine = new Routine();
        sampleRoutine.setId(100L);
        sampleRoutine.setIdAssignment(sampleAssignment);
        sampleRoutine.setName("Rutina Hipertrofia");
        sampleRoutine.setDescription("Fuerza e hipertrofia torso/pierna");
        sampleRoutine.setActive(true);
    }

    @Test
    void createRoutine_Success_PersistsRoutine() {
        CreateRoutineRequest request = new CreateRoutineRequest(10L, "  Rutina Hipertrofia  ", "Fuerza");

        when(assignmentRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(sampleAssignment));

        RoutineResponse expectedResponse = new RoutineResponse(
                100L, 10L, UUID.randomUUID(), "Carlos", UUID.randomUUID(), "Entrenador",
                "Rutina Hipertrofia", "Fuerza", true, Instant.now(), Instant.now()
        );
        when(mapper.toResponse(any(Routine.class))).thenReturn(expectedResponse);

        RoutineResponse response = routineService.createRoutine(request);

        assertNotNull(response);
        assertEquals("Rutina Hipertrofia", response.name());
        assertTrue(response.active());
        verify(repository, times(1)).persist(any(Routine.class));
    }

    @Test
    void createRoutine_WhenAssignmentNotFound_ThrowsNotFoundException() {
        CreateRoutineRequest request = new CreateRoutineRequest(999L, "Rutina", "Desc");
        when(assignmentRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> routineService.createRoutine(request));
        verify(repository, never()).persist(any(Routine.class));
    }

    @Test
    void updateRoutine_Success_UpdatesFields() {
        UpdateRoutineRequest request = new UpdateRoutineRequest("Rutina Definicion", "Nuevo plan", false);
        when(repository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleRoutine));

        RoutineResponse expectedResponse = new RoutineResponse(
                100L, 10L, UUID.randomUUID(), "Carlos", UUID.randomUUID(), "Entrenador",
                "Rutina Definicion", "Nuevo plan", false, Instant.now(), Instant.now()
        );
        when(mapper.toResponse(sampleRoutine)).thenReturn(expectedResponse);

        RoutineResponse response = routineService.updateRoutine(100L, request);

        assertNotNull(response);
        assertEquals("Rutina Definicion", sampleRoutine.getName());
        assertEquals("Nuevo plan", sampleRoutine.getDescription());
        assertFalse(sampleRoutine.getActive());
        verify(mapper, times(1)).toResponse(sampleRoutine);
    }

    @Test
    void updateRoutine_WhenRoutineNotFound_ThrowsNotFoundException() {
        UpdateRoutineRequest request = new UpdateRoutineRequest("Rutina", "Desc", true);
        when(repository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> routineService.updateRoutine(999L, request));
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void findResponseById_Success_ReturnsMappedResponse() {
        when(repository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleRoutine));

        RoutineResponse expectedResponse = new RoutineResponse(
                100L, 10L, UUID.randomUUID(), "Carlos", UUID.randomUUID(), "Entrenador",
                "Rutina Hipertrofia", "Fuerza", true, Instant.now(), Instant.now()
        );
        when(mapper.toResponse(sampleRoutine)).thenReturn(expectedResponse);

        RoutineResponse response = routineService.findResponseById(100L);

        assertNotNull(response);
        assertEquals(100L, response.id());
        verify(repository, times(1)).findByIdWithDetails(100L);
    }
}
