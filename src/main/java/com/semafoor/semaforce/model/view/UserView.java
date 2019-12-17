package com.semafoor.semaforce.model.view;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class that presents a summary of a User entity to API consumers.
 * This view can be created using JPQL constructor queries.
 */
@Data
public class UserView {

    private Long id;
    private String username;
    private String email;
    private WorkoutView currentWorkout;

    // Constructor which can be used when the user already has a CurrentWorkout
    public UserView(Long id, String username, String email, Long workoutId, String referenceName, int numWeeks, int daysPerWeek, int currentDay, LocalDateTime workoutStartDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.currentWorkout = new WorkoutView(workoutId, referenceName, numWeeks, daysPerWeek, currentDay, workoutStartDate);
    }

    // Constructor which should be used when the user does not have a CurrentWorkout
    public UserView(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.currentWorkout = null;
    }
}
