package com.semafoor.semaforce.model.entities.workout;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Embeddable class which defines a logical composition of colomns/ properties on the ScheduledExercise embeddable.
 * These columns are stored in the TRAINING_DAY_SCHEDULED_EXERCISES collection table.
 */
@Data
@Embeddable
public class ExerciseConfiguration {

    @NotNull(message = "Number of sets must be set")
    private int numSets;

    @NotNull(message = "Minimum number of reps must be set")
    private int minReps;

    @NotNull(message = "Maximum number of reps must be set")
    private int maxReps;

    @NotNull(message = "Rest period between sets must be set")
    private int restBetweenSets;

    ExerciseConfiguration() {
    }

    public ExerciseConfiguration(int numSets, int minReps, int maxReps, int restBetweenSets) {
        this.numSets = numSets;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.restBetweenSets = restBetweenSets;
    }
}
