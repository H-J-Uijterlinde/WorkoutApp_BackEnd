package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.goals.Goals;
import com.semafoor.semaforce.model.entities.goals.TotalWorkouts;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.view.GoalsView;
import com.semafoor.semaforce.repositories.GoalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GoalsService {

    private final GoalsRepository goalsRepository;

    @Autowired
    public GoalsService(GoalsRepository goalsRepository) {
        this.goalsRepository = goalsRepository;
    }

    public Goals postNewGoal(Goals goal) {
        return this.goalsRepository.save(goal);
    }

    @Transactional(readOnly = true)
    public List<Goals> findAllGoalsByUserId(Long id) {
        return Lists.newArrayList(this.goalsRepository.findAllByUser_Id(id));
    }

    @Transactional(readOnly = true)
    public List<GoalsView> getAllGoalsViews(Long id) {
        return goalsRepository.getAllGoalsViews(id);
    }

    void updateActiveGoals(TrainingDay trainingDay) {
        Long userId = trainingDay.getScheduledExercises().get(1).getResults().getUser().getId();
        List<Goals> goals = this.findAllGoalsByUserId(userId);
        goals.stream().filter( goal -> goal instanceof TotalWorkouts).forEach( goal -> ((TotalWorkouts) goal).updateCompletionPercentage());
    }
}
