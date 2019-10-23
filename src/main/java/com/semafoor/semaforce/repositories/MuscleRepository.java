package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.view.MuscleView;
import com.semafoor.semaforce.model.entities.exercise.Muscle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for CRUD database operations on the Muscle entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface MuscleRepository extends CrudRepository<Muscle, Long> {

    List<MuscleView> getAllMuscleViews();

    List<MuscleView> getMuscleViewsByExerciseId(@Param("id") Long id);

}
