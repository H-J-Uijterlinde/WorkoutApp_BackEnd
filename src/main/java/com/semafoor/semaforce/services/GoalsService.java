package com.semafoor.semaforce.services;

import com.google.common.collect.Lists;
import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.entities.goals.ExerciseGoals;
import com.semafoor.semaforce.model.entities.goals.Goals;
import com.semafoor.semaforce.model.entities.goals.TotalWorkouts;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.view.GoalsView;
import com.semafoor.semaforce.repositories.GoalsRepository;
import com.semafoor.semaforce.repositories.ResultRepository;
import com.semafoor.semaforce.services.utilities.ResultUtilities;
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
    private final ResultUtilities resultUtils;

    @Autowired
    public GoalsService(GoalsRepository goalsRepository, ResultRepository resultRepository, ResultUtilities resultUtils) {
        this.goalsRepository = goalsRepository;
        this.resultRepository = resultRepository;
        this.resultUtils = resultUtils;
    }

    @Transactional(readOnly = true)
    public List<Goals> findAllGoalsByUserId(Long id) {
        return Lists.newArrayList(this.goalsRepository.findAllByUser_IdAndActive(id, true));
    }

    @Transactional(readOnly = true)
    public List<GoalsView> getAllGoalsViews(Long id, boolean isActive) {
        return goalsRepository.getAllGoalsViews(id, isActive);
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
        // get results for current user for exercise corresponding with the goal
        // Todo: check only results from last year
        List<Result> resultsForExercise = resultRepository.findAllByExerciseAndUser(goal.getExercise(), goal.getUser());

        // obtain a stream of relevant weekly results
        Stream<WeeklyResult> relevantWeeklyResults = this.getWeeklyResults(goal, resultsForExercise);


        OptionalDouble startingVolume = relevantWeeklyResults.
                mapToDouble(weeklyResult -> this.calculateEstimatedTotalVolume(goal, weeklyResult)).max();

        goal.setStartingVolume((int) startingVolume.orElse(0));
    }

    private Stream<WeeklyResult> getWeeklyResults(ExerciseGoals goal, List<Result> resultsForExercise) {

        Stream<WeeklyResult> weeklyResultStream = resultsForExercise.stream().

                // Generates a single stream of weekly results
                        flatMap(result -> result.getWeeklyResults().values().stream());

        return this.filterForSetsRepsWeight(goal, weeklyResultStream);
    }

    private Stream<WeeklyResult> filterForSetsRepsWeight(ExerciseGoals goal, Stream<WeeklyResult> weeklyResultStream) {

        // filter for results where the number of sets is at most 1 less then desired number of sets from the goal.
        return weeklyResultStream.filter(weeklyResults -> weeklyResults.getNumbersLifted().size() >= goal.getDesiredSets() - 1).

                // filter for results where the average number of repetitions is between the desired reps of the goal
                // plus or minus three. Or results where the average weight lifted is within a 5% margin of the desired weight
                        filter(weeklyResult -> (this.resultUtils.getAverageRepsPerformed(weeklyResult) >= goal.getDesiredReps() - 3
                        && this.resultUtils.getAverageRepsPerformed(weeklyResult) <= goal.getDesiredReps() + 3) ||
                        (this.resultUtils.getAverageWeightLifted(weeklyResult) >= goal.getDesiredWeight() - (goal.getDesiredWeight() * 0.05) &&
                                this.resultUtils.getAverageWeightLifted(weeklyResult) <= goal.getDesiredWeight() + (goal.getDesiredWeight() * 0.05)));
    }

    private double calculateEstimatedTotalVolume(ExerciseGoals goal, WeeklyResult weeklyResult) {

        double weightForDesiredRepNumber = 0;

        if (goal.getDesiredSets() == 1) {
            weightForDesiredRepNumber = this.resultUtils.getWeightForDesiredRepNumber(goal.getDesiredReps(), weeklyResult);
        } else if (goal.getDesiredSets() > 1) {
            weightForDesiredRepNumber = this.resultUtils.getWeightForDesiredRepNumberForMultipleSets(goal.getDesiredReps(), weeklyResult);
        }

        return Math.floor(weightForDesiredRepNumber * goal.getDesiredReps() * goal.getDesiredSets());
    }

    public void updateActiveGoals(Long userId, List<WeeklyResultDto> weeklyResultDtos) {

        List<Goals> goals = this.findAllGoalsByUserId(userId);

        // update the TotalWorkout goals
        goals.stream().filter(goal -> goal instanceof TotalWorkouts).forEach(goal -> {
            ((TotalWorkouts) goal).updateCompletionPercentage();
            if (goal.getCompletionPercentage() >= 100) {
                goal.setActive(false);
            }
        });

        // check if current exercise goals need to be updated
        goals.stream().filter(goal -> goal instanceof ExerciseGoals).forEach(goal -> this.updateCompletionPercentage((ExerciseGoals) goal, weeklyResultDtos));
    }

    private void updateCompletionPercentage(ExerciseGoals goal, List<WeeklyResultDto> weeklyResultDtos) {

        // collect relevant results
        Stream<WeeklyResult> weeklyResultsForExercise = weeklyResultDtos.stream().
                filter(weeklyResultDto -> weeklyResultDto.getExerciseId().equals(goal.getExercise().getId())).
                map(WeeklyResultDto::transform);

        Stream<WeeklyResult> relevantResults = this.filterForSetsRepsWeight(goal, weeklyResultsForExercise);

        // if starting volume is set, check if completion percentage needs to be updated.
        if (goal.getStartingVolume() != 0) {
            relevantResults.mapToDouble(weeklyResult -> {
                double totalVolume = calculateEstimatedTotalVolume(goal, weeklyResult);
                double desiredTotalVolume = goal.getDesiredReps() * goal.getDesiredSets() * goal.getDesiredWeight();
                return ((totalVolume - goal.getStartingVolume()) / (desiredTotalVolume - goal.getStartingVolume())) * 100;
            }).forEach(completionPercentage -> {
                if (completionPercentage > goal.getCompletionPercentage()) {
                    goal.setCompletionPercentage((int) completionPercentage);
                    if (completionPercentage >= 100) {
                        goal.setActive(false);
                    }
                }
            });
            // else if starting volume has not been set, check if starting volume can be set from relevant results.
        } else if (goal.getStartingVolume() == 0) {
            OptionalDouble startingVolume = relevantResults.
                    mapToDouble(weeklyResult -> this.calculateEstimatedTotalVolume(goal, weeklyResult)).max();

            goal.setStartingVolume((int) startingVolume.orElse(0));
        }
    }
}
