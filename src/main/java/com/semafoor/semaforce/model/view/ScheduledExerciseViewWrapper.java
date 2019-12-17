package com.semafoor.semaforce.model.view;

import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduledExerciseViewWrapper {

    private List<ScheduledExerciseView> scheduledExerciseViews = new ArrayList<>();

    public static ScheduledExerciseViewWrapper transformToView(TrainingDay trainingDay) {

        ScheduledExerciseViewWrapper view = new ScheduledExerciseViewWrapper();
        trainingDay.getScheduledExercises().values().forEach(
                scheduledExercise -> view.scheduledExerciseViews.add(ScheduledExerciseView.transformToView(scheduledExercise))
        );
        return view;
    }
}
