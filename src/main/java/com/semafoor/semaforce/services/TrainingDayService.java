package com.semafoor.semaforce.services;

import com.semafoor.semaforce.model.dto.WeeklyResultDto;
import com.semafoor.semaforce.model.entities.result.Result;
import com.semafoor.semaforce.model.entities.workout.TrainingDay;
import com.semafoor.semaforce.repositories.TrainingDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainingDayService {

    @Autowired
    private TrainingDayRepository trainingDayRepository;

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
