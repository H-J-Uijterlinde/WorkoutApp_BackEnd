package com.semafoor.semaforce.model.dto.workout;

import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Class used to receive JSON objects which can be transformed into a TrainingDay entity.
 */
@Data
public class TrainingDayDto {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDayDto.class);

    private User user;
    private int dayNumber;
    private List<ScheduledExercise> scheduledExercises;

    /**
     * Method that can be called on a TrainingDayDto object, to transform it into a TrainingDay entity
     * @return  TrainingDay entity
     */
    public TrainingDay transform() {

        TrainingDay trainingDay = new TrainingDay(this.dayNumber);
        for (int i = 0; i < scheduledExercises.size(); i++) {
            ScheduledExercise scheduledExercise = scheduledExercises.get(i);
            scheduledExercise.setResults(new Result(this.user, scheduledExercise.getExercise(), new HashMap<>()));
            trainingDay.getScheduledExercises().put(i + 1, scheduledExercise);
        }
        return trainingDay;
    }
}
