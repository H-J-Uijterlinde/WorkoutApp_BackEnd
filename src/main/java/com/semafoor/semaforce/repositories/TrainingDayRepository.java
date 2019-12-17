package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.view.TrainingDayView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository responsible for CRUD database operations on the TrainingDay entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface TrainingDayRepository extends CrudRepository<TrainingDay, Long> {

    List<TrainingDayView> getTrainingDaysByInstantWorkoutAndUserId(@Param("userId") Long userId);
}
