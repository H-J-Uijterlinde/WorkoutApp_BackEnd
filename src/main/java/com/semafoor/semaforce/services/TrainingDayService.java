package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.dto.WeeklyResultDto;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.repositories.TrainingDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service class serves as the bridge between the data access layer and the controller layer for the TrainingDay
 * entity. This class is where the business logic can be implemented.
 */
@Service
@Transactional
public class TrainingDayService {

    @Autowired
    private TrainingDayRepository trainingDayRepository;

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

        for (WeeklyResultDto dto: weeklyResultDtos) {
            Result result = trainingDay.getScheduledExercises().get(dto.getExerciseNumber()).getResults();
            result.addResult(dto.transform());
        }

        trainingDay.setCurrentWeek(trainingDay.getCurrentWeek() + 1);
        return trainingDayRepository.save(trainingDay);
    }
}
