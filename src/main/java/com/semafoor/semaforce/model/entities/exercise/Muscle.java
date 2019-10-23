package com.semafoor.semaforce.model.entities.exercise;

import com.semafoor.semaforce.model.entities.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Defines the Muscle entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Muscle.getAllMuscleViews",
                query = "select new com.semafoor.semaforce.model.view.MuscleView(M.id, M.name, M.scientificName) from Muscle M"
        ),
        @NamedQuery(
                name = "Muscle.getMuscleViewsByExerciseId",
                query = "select new com.semafoor.semaforce.model.view.MuscleView(M.id, M.name, M.scientificName) " +
                        "from Exercise E " +
                        "join E.musclesTrained M " +
                        "where E.id = :id"
        )
})
public class Muscle extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "Muscle name must be set")
    @Size(
            min = 2,
            max = 255,
            message = "Name is required. Minimum is 2, maximum is 255 characters"
    )
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Muscle scientific name must be set")
    @Size(
            min = 2,
            max = 255,
            message = "Scientific name is required. Minimum is 2, maximum is 255 characters"
    )
    @Column(nullable = false, unique = true)
    private String scientificName;

    Muscle() {
    }

    public Muscle(String name, String scientificName) {
        this.name = name;
        this.scientificName = scientificName;
    }
}
