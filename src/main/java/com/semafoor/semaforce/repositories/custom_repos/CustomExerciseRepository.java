package com.semafoor.semaforce.repositories.custom_repos;

import com.semafoor.semaforce.model.entities.exercise.Category;
import com.semafoor.semaforce.model.view.ExerciseView;

import java.util.List;

/**
 * This interface was created to provide extra methods to the ExerciseRepository. The methods defined here should be
 * implemented by a concrete CustomExerciseRepository implementation class.
 */
public interface CustomExerciseRepository {

    List<ExerciseView> findExercisesByCriteriaQuery(String name, Category category, Long muscleId);
}
