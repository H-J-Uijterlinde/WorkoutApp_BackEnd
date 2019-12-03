package com.semafoor.semaforce.model.entities.workout;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import com.semafoor.semaforce.model.entities.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the Workout entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Workout.deleteAllTrainingDays",
                query = "DELETE from TrainingDay T where T.workout IN :workouts"
        ),
        @NamedQuery(
                name = "Workout.getTrainingDay",
                query = "select T from Workout W " +
                        "join W.trainingDays as T " +
                        "where W.id = :id and KEY(T) = :dayNumber"
        ),
        @NamedQuery(
                name = "Workout.getAllTrainingDaysFromWorkout",
                query = "Select T from Workout W " +
                        "join W.trainingDays as T " +
                        "where W.id = :id"
        ),
        @NamedQuery(
                name = "Workout.getInstantWorkoutByUserId",
                query = "select W from Workout W " +
                        "join W.user as U " +
                        "where U.id = :userId and W.isInstantWorkout = true"
        )
})
public class Workout extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull(message = "The user for this workout must be set")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @NotNull(message = "Reference name of this workout must be set")
    private String referenceName;

    @NotNull(message = "Number of consecutive weeks must be set")
    private int numWeeks;

    @NotNull(message = "Number of training days per week must be set")
    private int daysPerWeek;

    private int currentDay;

    @OneToMany(mappedBy = "workout",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @MapKey(name = "dayNumber")
    private Map<Integer, TrainingDay> trainingDays = new HashMap<>();

    @NotNull(message = "Please indicate if this workout is created for instant workout purposes")
    private boolean isInstantWorkout;

    Workout() {
    }

    public Workout(User user, String referenceName, int numWeeks, int daysPerWeek, boolean isInstantWorkout) {
        this.user = user;
        this.referenceName = referenceName;
        this.numWeeks = numWeeks;
        this.daysPerWeek = daysPerWeek;
        this.isInstantWorkout = isInstantWorkout;
    }

    public void addTrainingDay(TrainingDay trainingDay) {
        trainingDay.setWorkout(this);
        this.trainingDays.put(trainingDay.getDayNumber(), trainingDay);
    }
}
