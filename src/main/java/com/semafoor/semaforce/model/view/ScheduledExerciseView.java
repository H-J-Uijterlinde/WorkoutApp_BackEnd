package com.semafoor.semaforce.model.view;

import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import lombok.Data;

@Data
class ScheduledExerciseView {

    private String exerciseName;
    private int minReps;
    private int maxReps;
    private int numSets;
    private int restBetweenSets;

    static ScheduledExerciseView transformToView(ScheduledExercise exercise) {

        ScheduledExerciseView view = new ScheduledExerciseView();
        view.exerciseName = exercise.getExercise().getName();
        view.minReps = exercise.getConfiguration().getMinReps();
        view.maxReps = exercise.getConfiguration().getMaxReps();
        view.numSets = exercise.getConfiguration().getNumSets();
        view.restBetweenSets = exercise.getConfiguration().getRestBetweenSets();

        return view;
    }
}
