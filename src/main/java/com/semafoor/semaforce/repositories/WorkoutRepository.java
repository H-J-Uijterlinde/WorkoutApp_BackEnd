package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for CRUD database operations on the Exercise entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface WorkoutRepository extends CrudRepository<Workout, Long> {

    List<Workout> findAllByUser(User user);

    TrainingDay getTrainingDay(@Param("id") Long workoutId, @Param("dayNumber") int dayNumber);

    List<TrainingDay> getAllTrainingDaysFromWorkout(@Param("id") Long workoutId);

    Optional<Workout> getInstantWorkoutByUserId(@Param("userId") Long userId);

    @Modifying
    void deleteAllTrainingDays(@Param("workouts") List<Workout> workouts);
}
