package com.semafoor.semaforce.model.entities.workout;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the TrainingDay entity.
 */
@Data
@Entity
public class TrainingDay{

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

    TrainingDay(){

    }

    public TrainingDay(int dayNumber) {
        this.dayNumber = dayNumber;
        this.currentWeek = 1;
    }
}
