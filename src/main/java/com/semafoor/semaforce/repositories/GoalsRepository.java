package com.semafoor.semaforce.repositories;

import com.semafoor.semaforce.model.entities.goals.Goals;
import com.semafoor.semaforce.model.view.GoalsView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoalsRepository extends CrudRepository<Goals, Long> {

    List<Goals> findAllByUser_IdAndActive(Long id, boolean isActive);

    List<GoalsView> getAllGoalsViews(@Param("userId") Long userId, @Param("isActive") boolean isActive);
}
