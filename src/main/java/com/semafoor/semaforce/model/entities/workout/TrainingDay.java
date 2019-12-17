package com.semafoor.semaforce.model.entities.workout;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semafoor.semaforce.model.entities.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the TrainingDay entity.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@NamedQueries({
        @NamedQuery(
                name = "TrainingDay.getTrainingDaysByInstantWorkoutAndUserId",
                query = "select new com.semafoor.semaforce.model.view.TrainingDayView(T.id, T.createdDateTime) from TrainingDay T " +
                        "join T.workout as W join W.user as U " +
                        "where W.isInstantWorkout = true and U.id = :userId"
        )
})
public class TrainingDay extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Workout workout;

    @NotNull(message = "Day number must be set")
    @Min(value = 1, message = "Day number starts at 1")
    @Column(nullable = false, name = "DAY_NUMBER")
    private int dayNumber;

    @Column(nullable = false)
    private int currentWeek;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TRAINING_DAY_SCHEDULED_EXERCISES")
    @MapKeyColumn(name = "EXERCISE_NUMBER")
    private Map<Integer, ScheduledExercise> scheduledExercises = new HashMap<>();

    public Long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public TrainingDay(){
    }

    public TrainingDay(int dayNumber) {
        this.dayNumber = dayNumber;
        this.currentWeek = 1;
    }
}
