package org.nova.domain.model.workout;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.nova.domain.model.member.Member;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "workout_logs", schema = "public")
public class WorkoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_member", nullable = false)
    private Member idMember;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_routine_exercise", nullable = false)
    private RoutineExercise idRoutineExercise;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "actual_sets")
    private Short actualSets;

    @Column(name = "actual_reps")
    private Short actualReps;

    @Column(name = "actual_weight", precision = 6, scale = 2)
    private BigDecimal actualWeight;

    @Column(name = "observation", length = Integer.MAX_VALUE)
    private String observation;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}