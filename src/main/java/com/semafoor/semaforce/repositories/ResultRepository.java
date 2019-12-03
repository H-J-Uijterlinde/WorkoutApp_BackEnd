package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for CRUD database operations on the Result entity.
 * Queries for the function which are not defined through the CrudRepository interface are defined as named queries on
 * the corresponding class.
 */
public interface ResultRepository extends CrudRepository<Result, Long> {

    List<Result> findAllByExerciseAndUser(Exercise exercise, User user);

    Optional<Result> findInstantTrainingResultsByExerciseIdAndUserId(@Param("userId") Long userId,
                                                                 @Param("exerciseId") Long exerciseId);
}
