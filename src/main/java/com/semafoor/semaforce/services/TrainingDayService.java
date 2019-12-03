package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.dto.results.WeeklyResultDto;
import com.semafoor.semaforce.model.dto.workout.TrainingDayDto;
import com.semafoor.semaforce.model.entities.exercise.Exercise;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import com.semafoor.semaforce.model.entities.user.User;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.model.entities.workout.Workout;
import com.semafoor.semaforce.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the TrainingDay
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class TrainingDayService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDayService.class);

    private final TrainingDayRepository trainingDayRepository;
    private final GoalsService goalsService;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public TrainingDayService(TrainingDayRepository trainingDayRepository, GoalsService goalsService, WorkoutRepository workoutRepository, UserRepository userRepository, ResultRepository resultRepository, ExerciseRepository exerciseRepository) {
        this.trainingDayRepository = trainingDayRepository;
        this.goalsService = goalsService;
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Method that adds new weekly results to a TrainingDay entity. Also updates the currentWeek number.
     *
     * @param trainingDayId Id of the TrainingDay entity the results need to be added to.
     * @param weeklyResultDtos List of new WeeklyResult entities.
     *
     * @return The updated TrainingDay entity
     */
    public TrainingDay addWeeklyResult(Long trainingDayId, List<WeeklyResultDto> weeklyResultDtos) {

        TrainingDay trainingDay = trainingDayRepository.findById(trainingDayId).orElseThrow(() -> new RuntimeException("TrainingDay not found."));

        addWeeklyResultsToResult(weeklyResultDtos, trainingDay);

        this.goalsService.updateActiveGoals(trainingDay.getWorkout().getUser().getId(), weeklyResultDtos);

        trainingDay.setCurrentWeek(trainingDay.getCurrentWeek() + 1);
        return trainingDayRepository.save(trainingDay);
    }

    private void addWeeklyResultsToResult(List<WeeklyResultDto> weeklyResultDtos, TrainingDay trainingDay) {
        for (WeeklyResultDto dto: weeklyResultDtos) {
            if (trainingDay.getScheduledExercises().containsKey(dto.getExerciseNumber())) {
                logger.debug("Adding weekly result for exercise with id: " + dto.getExerciseId() + "to regular workout results");
                Result result = trainingDay.getScheduledExercises().get(dto.getExerciseNumber()).getResults();
                result.addResult(dto.transform());
            } else {
                this.addWeeklyResultsToInstantTrainingResult(trainingDay.getWorkout().getUser(), dto);
            }
        }
    }

    public TrainingDay addNewInstantTrainingDay(Long userId, TrainingDayDto trainingDayDto, List<WeeklyResultDto> weeklyResultDtos) {

        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Workout> optionalWorkout = workoutRepository.getInstantWorkoutByUserId(userId);
        TrainingDay trainingDay = trainingDayDto.transform();
        Workout workout;

        if (optionalWorkout.isPresent()) {

            workout = optionalWorkout.get();
            int trainingDayNumber = (workout.getTrainingDays().size() + 1);
            trainingDay.setDayNumber(trainingDayNumber);


        } else {

            workout = new Workout(currentUser, "Instant training day holder",
                    1, 1, true);
            trainingDay.setDayNumber(1);

        }

        workout.addTrainingDay(trainingDay);
        weeklyResultDtos.forEach(
                weeklyResultDto -> this.addWeeklyResultsToInstantTrainingResult(currentUser, weeklyResultDto)
        );

        this.goalsService.updateActiveGoals(userId, weeklyResultDtos);
        this.workoutRepository.save(workout);
        return trainingDay;
    }

    private void addWeeklyResultsToInstantTrainingResult(User user, WeeklyResultDto weeklyResultDto) {

        logger.debug("Trying to find instant training result for exercise with id: " + weeklyResultDto.getExerciseId());
        Optional<Result> relevantResult = this.resultRepository.findInstantTrainingResultsByExerciseIdAndUserId(user.getId(), weeklyResultDto.getExerciseId());
        Result result;

        if (relevantResult.isPresent()) {

            logger.debug("Instant training result found, already exists.");
            result = relevantResult.get();

        } else {

            logger.debug("No Instant training result found, creating new one");
            Exercise exerciseForResult = this.exerciseRepository.findById(weeklyResultDto.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));
            result = new Result(user, exerciseForResult, new HashMap<>());

            result.setInstantTrainingResult(true);
            logger.debug("Result isInstantTrainingResult is set to: " + result.isInstantTrainingResult());
        }

        int weekNumber = (result.getWeeklyResults().size() + 1);
        logger.debug("Week number of instant training new weekly result set to: " + weekNumber);
        weeklyResultDto.setWeekNumber(weekNumber);
        WeeklyResult weeklyResult = weeklyResultDto.transform();
        result.addResult(weeklyResult);
        result.getWeeklyResults().values().forEach( weeklyResult1 -> logger.debug("Weekly result with week number: " + weeklyResult1.getWeekNumber() +
                " has a result set with id: " + weeklyResult1.getResult().getId()));

        this.resultRepository.save(result);
    }
}
