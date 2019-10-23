package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.view.ExerciseView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for CRUD database operations on the Exercise entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    List<ExerciseView> getExerciseViews();

    @Modifying
    void deletePrimaryMuscleTrained(@Param("id") Long id);
}
