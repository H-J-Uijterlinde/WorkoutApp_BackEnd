package com.semafoor.semaforce.model.dto.goals;

import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.goals.ExerciseGoals;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;

@Data
public class ExerciseGoalsDto {

    User user;
    Exercise exercise;
    int desiredReps;
    int desiredSets;
    int desiredWeight;

    public ExerciseGoalsDto() {}

    public ExerciseGoals transform() {
        String title = exercise.getName();
        String subTitle;

        if (this.desiredSets > 1) {
            subTitle = this.desiredSets + " x " + this.desiredReps + " with " + this.desiredWeight + " kg";
        } else {
            subTitle = this.desiredReps + " x " + this.desiredWeight + " kg";
        }

        return new ExerciseGoals(this.user, true, 0, this.exercise,
                this.desiredReps, this.desiredSets, this.desiredWeight, title, subTitle);
    }
}
