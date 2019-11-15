package com.semafoor.semaforce.model.entities.goals;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@DiscriminatorValue("EG")
public class ExerciseGoals extends Goals {

    @NotNull(message = "The exercise for this goal must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Exercise exercise;

    @NotNull(message = "The desired number of repetitions for this goal must be set")
    private int desiredReps;

    @NotNull(message = "The desired number of sets for this goals must be set")
    private int desiredSets;

    @NotNull(message = "The desired weight for this goal must be set")
    private int desiredWeight;

    @NotNull(message = "A starting volume for this goal must be set")
    private int startingVolume;

    ExerciseGoals() {
    }

    public ExerciseGoals(User user, boolean active, int completionPercentage, Exercise exercise, int desiredReps,
                         int desiredSets, int desiredWeight, String title, String subTitle, int startingVolume) {
        this.user = user;
        this.active = active;
        this.completionPercentage = completionPercentage;
        this.exercise = exercise;
        this.desiredReps = desiredReps;
        this.desiredSets = desiredSets;
        this.desiredWeight = desiredWeight;
        this.title = title;
        this.subTitle = subTitle;
        this.startingVolume= startingVolume;
    }
}
