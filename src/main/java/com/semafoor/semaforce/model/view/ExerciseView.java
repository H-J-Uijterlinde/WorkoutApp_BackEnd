package com.semafoor.semaforce.model.view;

import com.semafoor.semaforce.model.entities.exercise.Category;
import lombok.Data;

/**
 * Class that presents a summary of an Exercise entity to API consumers.
 * This view can be created using JPQL constructor queries.
 */
@Data
public class ExerciseView {

    private Long id;
    private String name;
    private String primaryMuscle;
    private Category category;

    public ExerciseView(Long id, String name, String primaryMuscle, Category category) {
        this.id = id;
        this.name = name;
        this.primaryMuscle = primaryMuscle;
        this.category = category;
    }
}
