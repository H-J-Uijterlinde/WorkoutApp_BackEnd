package com.semafoor.semaforce.model.view;

import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrainingDayView {

    private List<ScheduledExerciseView> scheduledExerciseViews = new ArrayList<>();

    public static TrainingDayView transformToView(TrainingDay trainingDay) {

        TrainingDayView view = new TrainingDayView();
        trainingDay.getScheduledExercises().values().forEach(
                scheduledExercise -> view.scheduledExerciseViews.add(ScheduledExerciseView.transformToView(scheduledExercise))
        );
        return view;
    }
}
