package com.semafoor.semaforce.services.utilities;

import com.semafoor.semaforce.model.entities.result.Score;
import com.semafoor.semaforce.model.entities.result.WeeklyResult;
import org.springframework.stereotype.Service;

import java.util.OptionalDouble;

@Service
public class ResultUtilities {

    public double getAverageRepsPerformed(WeeklyResult weeklyResult) {

        OptionalDouble averageRepsForWeeklyResult = weeklyResult.getNumbersLifted().values().stream().
                mapToDouble(Score -> Score.getRepetitionsPerformed() + (10 - Score.getRpe())).average();

        return averageRepsForWeeklyResult.orElseThrow(() -> new RuntimeException("No repetitions have been set for this result"));
    }

    public double getAverageWeightLifted(WeeklyResult weeklyResult) {

        OptionalDouble averageWeightForWeeklyResult = weeklyResult.getNumbersLifted().values().stream().
                mapToDouble(Score::getWeightLifted).average();

        return averageWeightForWeeklyResult.orElseThrow(() -> new RuntimeException("No weights have been set for this result"));
    }

    public double getWeightForDesiredRepNumber(double numReps, WeeklyResult weeklyResult) {

        double estimatedOneRepMax = this.getAverageWeightLifted(weeklyResult) *
                (1 + this.getAverageRepsPerformed(weeklyResult) / 30);

        return estimatedOneRepMax / (1 + (numReps / 30));
    }
}
