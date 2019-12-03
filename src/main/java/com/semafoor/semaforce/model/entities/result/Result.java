package com.semafoor.semaforce.model.entities.result;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.ScheduledExercise;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the Result entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Result.findInstantTrainingResultsByExerciseIdAndUserId",
                query = "select R from Result R " +
                        "join R.user as U join R.exercise as E " +
                        "where U.id = :userId and E.id = :exerciseId and R.isInstantTrainingResult = true"
        )
})
public class Result extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "The user for this result must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @NotNull(message = "The exercise for this result must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @OneToMany(mappedBy = "result",
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @MapKey(name = "weekNumber")
    private Map<Integer, WeeklyResult> weeklyResults = new HashMap<>();

    @NotNull(message = "Please indicate if this results comes from an instant training workout")
    private boolean isInstantTrainingResult;

    Result() {
    }

    public Result(User user, Exercise exercise, Map<Integer, WeeklyResult> weeklyResults) {
        this.user = user;
        this.exercise = exercise;
        this.weeklyResults = weeklyResults;
    }

    public void addResult(WeeklyResult weeklyResult) {
        weeklyResult.setResult(this);
        this.weeklyResults.put(weeklyResult.getWeekNumber(), weeklyResult);
    }
}
