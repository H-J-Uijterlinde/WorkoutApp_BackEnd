package com.semafoor.semaforce.model.entities.result;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Embeddable class which defines a logical composition of columns/ properties of the WeeklyResult table/ entity.
 */
@Data
@Embeddable
public class Score {

    @NotNull(message = "Weight lifted must be set")
    @PositiveOrZero(message = "Weights lifted can not be negative")
    @Column(nullable = false)
    private double weightLifted;

    @NotNull(message = "Repetitions performed must be set")
    @PositiveOrZero(message = "Repetitions performed can not be negative")
    @Column(nullable = false)
    private int repetitionsPerformed;

    @NotNull(message = "Rpe must be set")
    @PositiveOrZero(message = "Rpe can not be negative")
    @Column(nullable = false)
    private double rpe;

    public Score() {
    }

    public Score(double weightLifted, int repetitionsPerformed, double rpe) {
        this.weightLifted = weightLifted;
        this.repetitionsPerformed = repetitionsPerformed;
        this.rpe = rpe;
    }
}
