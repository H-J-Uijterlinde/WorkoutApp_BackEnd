package com.semafoor.semaforce.model.entities.result;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Embeddable class which defines a logical composition of columns/ properties of the WeeklyResult table/ entity.
 */
@Data
@Embeddable
public class Score {

    @NotNull(message = "Weight lifted must be set")
    @Column(nullable = false)
    private double weightLifted;

    @NotNull(message = "Repetitions performed must be set")
    @Column(nullable = false)
    private int repetitionsPerformed;

    @NotNull(message = "Rpe must be set")
    @Column(nullable = false)
    private double rpe;

    Score() {
    }

    public Score(double weightLifted, int repetitionsPerformed, double rpe) {
        this.weightLifted = weightLifted;
        this.repetitionsPerformed = repetitionsPerformed;
        this.rpe = rpe;
    }
}
