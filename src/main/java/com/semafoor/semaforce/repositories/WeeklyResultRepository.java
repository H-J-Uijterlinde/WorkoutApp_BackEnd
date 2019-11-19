package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeeklyResultRepository extends CrudRepository<WeeklyResult, Long> {

    List<WeeklyResult> findByUserAndExerciseSortByDateAscending(@Param("userId") Long userId,
                                                                @Param("exerciseId") Long exerciseId);
}
