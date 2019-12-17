package com.semafoor.semaforce.model.view;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class that presents a summary of a Workout entity to API consumers.
 * This view can be created using JPQL constructor queries.
 */
@Data
public class WorkoutView {

    private Long id;
    private String referenceName;
    private int numWeeks;
    private int daysPerWeek;
    private int currentDay;
    private LocalDateTime startedDate;

    public WorkoutView(Long id, String referenceName, int numWeeks, int daysPerWeek, int currentDay, LocalDateTime startedDate) {
        this.id = id;
        this.referenceName = referenceName;
        this.numWeeks = numWeeks;
        this.daysPerWeek = daysPerWeek;
        this.currentDay = currentDay;
        this.startedDate = startedDate;
    }
}
