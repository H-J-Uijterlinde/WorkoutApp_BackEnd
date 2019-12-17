package com.semafoor.semaforce.model.entities.workout;

import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.result.Result;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Embeddable class which defines a logical composition of colomns/ properties of the TrainingDay entity. These columns
 * are stored in the TRAINING_DAY_SCHEDULED_EXERCISES collection table.
 */
@Data
@Embeddable
public class ScheduledExercise {

    @ManyToOne
    @NotNull(message = "Set the exercise")
    @JoinColumn(nullable = false)
    private Exercise exercise;

    @NotNull(message = "Set the configuration for this exercise")
    @Column(nullable = false)
    private ExerciseConfiguration configuration;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn
    private Result results;

    public ScheduledExercise() {

    }

    public ScheduledExercise(Exercise exercise, ExerciseConfiguration configuration) {
        this.exercise = exercise;
        this.configuration = configuration;
    }
}
