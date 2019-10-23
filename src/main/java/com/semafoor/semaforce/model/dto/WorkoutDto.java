package com.semafoor.semaforce.model.dto;

import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * Class used to receive JSON objects which can be transformed into a TrainingDay entity.
 */
@Data
public class WorkoutDto {

    private User user;
    private String referenceName;
    private int numWeeks;
    private int daysPerWeek;
    private List<TrainingDayDto> trainingDays;

    /**
     * Method that can be called on a WorkoutDto object, to transform it into a Workout entity
     * @return  Workout entity
     */
    public Workout transform() {
        Workout workout = new Workout(this.user, this.referenceName, this.numWeeks, this.daysPerWeek);

        // transform all TrainingDayDto's into TrainingDay entities
        for (TrainingDayDto trainingDayDto: trainingDays) {

            TrainingDay trainingDay = trainingDayDto.transform();

            // Create a new Result entity for all ScheduledExercises
            trainingDay.getScheduledExercises().forEach(
                    (k,v) -> v.setResults(new Result(this.user, v.getExercise(), new HashMap<>()))
            );
            workout.addTrainingDay(trainingDay);
        }
        return workout;
    }
}
