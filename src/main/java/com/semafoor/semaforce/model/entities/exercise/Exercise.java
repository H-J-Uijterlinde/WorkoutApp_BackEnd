package com.semafoor.semaforce.model.entities.exercise;

import com.semafoor.semaforce.model.entities.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines the Exercise entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Exercise.getExerciseViews",
                query = "select new com.semafoor.semaforce.model.view.ExerciseView(E.id, E.name, E.muscleGroup, E.category) from Exercise E "
        )
})
public class Exercise extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "The name for this exercise must be set")
    @Size(
            min = 2,
            max = 255,
            message = "Name is required. Minimum is 2, maximum is 255 characters"
    )
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "The muscles trained with this exercise must be set")
    @ManyToMany
    @JoinTable(
            inverseJoinColumns = @JoinColumn(name = "MUSCLE_ID")
    )
    private Set<Muscle> musclesTrained = new HashSet<>();

    @NotNull(message = "The muscle group for this exercise must be set")
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @NotNull(message = "The category for this exercise must be set")
    @Enumerated(EnumType.STRING)
    private Category category;

    public Exercise() {
    }

    public Exercise(String name, Set<Muscle> musclesTrained, MuscleGroup muscleGroup, Category category) {
        this.name = name;
        this.musclesTrained = musclesTrained;
        this.muscleGroup = muscleGroup;
        this.category = category;
    }
}
