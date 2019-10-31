package com.semafoor.semaforce.model.view;

import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.entities.exercise.MuscleGroup;
import lombok.Data;

/**
 * Class that presents a summary of an Exercise entity to API consumers.
 * This view can be created using JPQL constructor queries.
 */
@Data
public class ExerciseView {

    private Long id;
    private String name;
    private MuscleGroup muscleGroup;
    private Category category;

    public ExerciseView(Long id, String name, MuscleGroup muscleGroup, Category category) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.category = category;
    }
}
