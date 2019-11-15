package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.entities.goals.ExerciseGoals;
import com.semafoor.semaforce.model.entities.goals.Goals;
import com.semafoor.semaforce.model.entities.goals.TotalWorkouts;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.result.Score;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.view.GoalsView;
import com.semafoor.semaforce.repositories.GoalsRepository;
import com.semafoor.semaforce.repositories.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Service
@Transactional
public class GoalsService {

    private static final Logger logger = LoggerFactory.getLogger(GoalsService.class);

    private final GoalsRepository goalsRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public GoalsService(GoalsRepository goalsRepository, ResultRepository resultRepository) {
        this.goalsRepository = goalsRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional(readOnly = true)
    public List<Goals> findAllGoalsByUserId(Long id) {
        return Lists.newArrayList(this.goalsRepository.findAllByUser_Id(id));
    }

    @Transactional(readOnly = true)
    public List<GoalsView> getAllGoalsViews(Long id) {
        return goalsRepository.getAllGoalsViews(id);
    }

    public Goals postNewGoal(Goals goal) {

        if (goal instanceof ExerciseGoals) {
            logger.info("Attempting to set the starting volume");
            this.setStartingVolume((ExerciseGoals) goal);
        }
        
        return this.goalsRepository.save(goal);
    }

    public Goals updateGoal(Goals goal) {
        return this.goalsRepository.save(goal);
    }

    public void deleteGoal(Long goalId) {
        Goals goal = this.goalsRepository.findById(goalId).orElseThrow(() -> new RuntimeException("Goal not found"));
        this.goalsRepository.delete(goal);
    }

    private void setStartingVolume(ExerciseGoals goal) {
        List<Result> resultsForExercise = resultRepository.findAllByExerciseAndUser(goal.getExercise(), goal.getUser());

        // obtain a stream of relevant weekly results
        Stream<WeeklyResult> relevantWeeklyResults = getWeeklyResults(goal, resultsForExercise);


        OptionalDouble startingVolume = relevantWeeklyResults.
                mapToDouble( weeklyResult -> getAverageRepsPerformed(weeklyResult) * getAverageWeightLifted(weeklyResult)
                * goal.getDesiredSets()).max();

        goal.setStartingVolume((int) startingVolume.orElse(0));
    }

    private Stream<WeeklyResult> getWeeklyResults(ExerciseGoals goal, List<Result> resultsForExercise) {

        return resultsForExercise.stream().

                // Generates a single stream of weekly results
                flatMap(result -> result.getWeeklyResults().values().stream()).

                // filter for results where the number of sets is at most 1 less then desired number of sets from the goal.
                filter(weeklyResults -> weeklyResults.getNumbersLifted().size() >= goal.getDesiredSets() - 1).

                // filter for results where the average repetitions lays between the desired reps of the goal
                // plus or minus one. Or results where the average weight lifted is within a 5% margin of the desired weight
                filter( weeklyResult -> (getAverageRepsPerformed(weeklyResult) >= goal.getDesiredReps() - 1
                && getAverageRepsPerformed(weeklyResult) <= goal.getDesiredReps() + 1) ||
                (getAverageWeightLifted(weeklyResult) >= goal.getDesiredWeight() - (goal.getDesiredWeight() * 0.05) &&
                        getAverageWeightLifted(weeklyResult) <= goal.getDesiredWeight() + (goal.getDesiredWeight() * 0.05)));
    }

    private double getAverageRepsPerformed(WeeklyResult weeklyResult) {

        OptionalDouble averageRepsForWeeklyResult = weeklyResult.getNumbersLifted().values().stream().
                mapToInt(Score -> Score.getRepetitionsPerformed() + (10 - Score.getRpe())).average();

        logger.info("Average reps for weekly result: " + averageRepsForWeeklyResult.getAsDouble());
        return averageRepsForWeeklyResult.orElseThrow( () -> new RuntimeException("No repetitions have been set for this result"));
    }

    private double getAverageWeightLifted(WeeklyResult weeklyResult) {

        OptionalDouble averageWeightForWeeklyResult = weeklyResult.getNumbersLifted().values().stream().
                mapToInt(Score::getWeightLifted).average();

        logger.info("Average weight for weekly result: " + averageWeightForWeeklyResult.getAsDouble());
        return averageWeightForWeeklyResult.orElseThrow( () -> new RuntimeException("No weights have been set for this result"));
    }

    void updateActiveGoals(TrainingDay trainingDay) {
        Long userId = trainingDay.getScheduledExercises().get(1).getResults().getUser().getId();
        List<Goals> goals = this.findAllGoalsByUserId(userId);
        goals.stream().filter(goal -> goal instanceof TotalWorkouts).forEach(goal -> ((TotalWorkouts) goal).updateCompletionPercentage());
    }
}
